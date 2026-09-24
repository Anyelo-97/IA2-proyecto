(function(){
"use strict";

const API_BASE = (window.RUTAIA_API_BASE || "/api").replace(/\/$/, "");
const SESSION_KEY = "rutaia_session";
const ROLES = {ESTUDIANTE:"estudiante", ADMINISTRADOR:"admin"};

const EXAMPLES = [
  "Quiero aprender a crear páginas web.",
  "Necesito aprender Java para trabajar con Spring Boot.",
  "Me interesa analizar datos y construir paneles de control.",
  "Quiero automatizar procesos empresariales.",
  "¿Qué puedo estudiar para trabajar con inteligencia artificial?",
  "Quiero aprender a proteger aplicaciones web.",
  "Necesito implementar aplicaciones usando contenedores.",
  "Quiero aprender cocina italiana."
];

const state = {
  token: null,
  user: null,
  categorias: [],
  niveles: [],
  cursos: [],
  todosCursos: []
};


const $ = (id)=> document.getElementById(id);
const isAdmin = ()=> !!state.user && state.user.role==="admin";

function escapeHtml(str){
  return String(str ?? "").replace(/[&<>"']/g, m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[m]));
}
let toastTimer=null;
function toast(msg, isErr){
  const el = $("toast");
  el.textContent = msg;
  el.className = "toast show" + (isErr? " err":"");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(()=>{ el.classList.remove("show"); }, 3200);
}
const clearFieldError = (id)=>{
  const field = $(id);
  if(field) field.classList.remove("has-error");
};
const setFieldError = (id, msg)=>{
  const f = $(id);
  if(!f) return;
  f.classList.add("has-error");
  if(msg){ const t = f.querySelector(".error-text"); if(t) t.textContent = msg; }
};
function statusClass(estado){
  const k = estado.replace(/\s/g,"");
  return "status-" + k.charAt(0).toUpperCase() + k.slice(1).toLowerCase();
}
function fillSelect(el, items, {placeholder, value=(i)=>i.id, label=(i)=>i.nombre}={}){
  const prev = el.value;
  el.innerHTML = (placeholder!==undefined ? `<option value="">${escapeHtml(placeholder)}</option>` : "") +
    items.map(i=>`<option value="${escapeHtml(value(i))}">${escapeHtml(label(i))}</option>`).join("");
  if(prev) el.value = prev;
}
const fmtRating = (n)=> (n===null || n===undefined) ? "—" : Number(n).toFixed(1);
function normalizeUser(u){
  const source = u?.usuario || u?.user || u;
  const role = String(source?.rol || source?.role || "").toUpperCase();
  const normalizedRole = role === "ADMIN" || role === "ADMINISTRADOR" ? ROLES.ADMINISTRADOR :
    role === "ESTUDIANTE" ? ROLES.ESTUDIANTE : null;
  if(!source?.id || !normalizedRole) throw new Error("La respuesta de autenticación no es válida.");
  return {id:source.id, nombre:source.nombre || source.name || source.email || source.id, email:source.email || "", role:normalizedRole};
}


function saveSession(){
  try{
    if(state.token) localStorage.setItem(SESSION_KEY, JSON.stringify({token:state.token, user:state.user}));
    else localStorage.removeItem(SESSION_KEY);
  }catch(e){ toast("No se pudo guardar la sesión.", true); }
}
function loadSession(){
  try{ return JSON.parse(localStorage.getItem(SESSION_KEY)); }catch(e){ return null; }
}


async function http(path, {method="GET", body, auth=true}={}){
  const headers = {};
  if(body!==undefined) headers["Content-Type"] = "application/json";
  if(auth && state.token) headers["Authorization"] = "Bearer " + state.token;

  let res;
  try{
    res = await fetch(API_BASE + path, {method, headers, body: body!==undefined ? JSON.stringify(body) : undefined});
  }catch(e){
    throw new Error("No se pudo conectar con el servidor.");
  }
  if(res.status===401 && auth){
    logout();
    throw new Error("Tu sesión expiró. Inicia sesión de nuevo.");
  }
  if(!res.ok){
    let msg = res.status===401 ? "Correo/ID o contraseña incorrectos." : "Error del servidor ("+res.status+")";
    try{ const b = await res.json(); if(b.message || b.error) msg = b.message || b.error; }catch(e){}
    throw new Error(msg);
  }
  return res.status===204 ? null : res.json();
}
const enc = encodeURIComponent;

const api = {
  login:(identificador, password)=> http("/auth/login", {method:"POST", body:{identificador, password}, auth:false}),
  registrarEstudiante:(data)=> http("/auth/register/estudiante", {method:"POST", body:data, auth:false}),
  registrarAdmin:(data)=> http("/auth/register/admin", {method:"POST", body:data, auth:false}),
  me:()=> http("/auth/me"),
  categorias:()=> http("/categorias"),
  niveles:()=> http("/niveles"),
  cursos:(incluirInactivos)=> http("/cursos" + (incluirInactivos ? "?incluirInactivos=true" : "")),
  guardarCurso:(id, data)=> id ? http("/cursos/"+enc(id), {method:"PUT", body:data}) : http("/cursos", {method:"POST", body:data}),
  desactivarCurso:(id)=> http("/cursos/"+enc(id)+"/desactivar", {method:"PATCH"}),
  estudiantes:()=> http("/estudiantes"),
  crearEstudiante:(data)=> http("/estudiantes", {method:"POST", body:data}),
  eliminarEstudiante:(id)=> http("/estudiantes/"+enc(id), {method:"DELETE"}),
  consultar:(pregunta)=> http("/consultas", {method:"POST", body:{pregunta}}),
  historial:(estudianteId)=> http("/estudiantes/"+enc(estudianteId)+"/historial"),
  calificar:(recomendacionId, puntuacion, comentario)=>
    http("/recomendaciones/"+enc(recomendacionId)+"/calificacion", {method:"POST", body:{puntuacion, comentario}}),
  estadisticas:()=> http("/estadisticas")
};


const loginScreen = $("loginScreen");
const appRoot = $("appRoot");

function showLogin(){
  loginScreen.style.display = "flex";
  appRoot.style.display = "none";
}

const AUTH_FORMS = {
  "estudiante-login":"loginFormEstudiante", "estudiante-register":"registerFormEstudiante",
  "admin-login":"loginFormAdmin", "admin-register":"registerFormAdmin"
};
let authRole = "estudiante", authMode = "login";
function showAuthForm(){
  const active = AUTH_FORMS[authRole+"-"+authMode];
  Object.values(AUTH_FORMS).forEach(id=> $(id).classList.toggle("active", id===active));
  if(authMode==="register" && authRole==="estudiante" && state.categorias.length===0) loadPublicCategorias();
}
document.querySelectorAll(".role-toggle button").forEach(btn=>{
  btn.addEventListener("click", ()=>{
    document.querySelectorAll(".role-toggle button").forEach(b=>b.classList.remove("active"));
    btn.classList.add("active");
    authRole = btn.dataset.role;
    showAuthForm();
  });
});
document.querySelectorAll("[data-auth]").forEach(link=>{
  link.addEventListener("click", (e)=>{ e.preventDefault(); authMode = link.dataset.auth; showAuthForm(); });
});

function completeAuth(res, role){
  const token = res?.token || res?.accessToken;
  if(!token) throw new Error("El servidor no devolvió una sesión válida.");
  const user = normalizeUser(res);
  if(user.role!==role){
    throw new Error("Estas credenciales no corresponden a una cuenta de " + (role==="admin" ? "administrador." : "estudiante."));
  }
  state.token = res.token;
  state.user = user;
  saveSession();
  enterApp();
}

function bindLogin({formId, role, fieldId, idInput, passInput}){
  const form = $(formId);
  form.addEventListener("submit", async (e)=>{
    e.preventDefault();
    clearFieldError(fieldId);
    const identificador = $(idInput).value.trim();
    const password = $(passInput).value;
    if(!identificador){ setFieldError(fieldId, "Escribe tu correo o ID."); return; }
    if(!password){ setFieldError(fieldId, "Escribe tu contraseña."); return; }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true; btn.textContent = "Ingresando…";
    try{
      completeAuth(await api.login(identificador, password.trim()), role);
    }catch(err){
      setFieldError(fieldId, err.message);
    }finally{
      btn.disabled = false; btn.textContent = "Ingresar";
    }
  });
}
bindLogin({formId:"loginFormEstudiante", role:"estudiante", fieldId:"lf-est-email", idInput:"loginEstEmail", passInput:"loginEstPass"});
bindLogin({formId:"loginFormAdmin", role:"admin", fieldId:"lf-adm-email", idInput:"loginAdmEmail", passInput:"loginAdmPass"});


const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function bindRegister({formId, role, fields, read, call, btnText}){
  const form = $(formId);
  form.addEventListener("submit", async (e)=>{
    e.preventDefault();
    Object.values(fields).forEach(clearFieldError);
    const data = read();
    if(!data) return;
    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true; btn.textContent = "Creando cuenta…";
    try{
      const response = await call(data);
      form.reset();
      if(response?.token || response?.accessToken){
        completeAuth(response, role);
      }else{
        authMode = "login";
        showAuthForm();
        toast("Cuenta creada. Ahora inicia sesión.");
      }
    }catch(err){
      setFieldError(fields.email, err.message);
    }finally{
      btn.disabled = false; btn.textContent = btnText;
    }
  });
}
function checkCommon(f, i, data, pass2){
  let ok = true;
  if(data.nombre.length<2){ setFieldError(f.nombre, "Escribe tu nombre completo."); ok=false; }
  if(!EMAIL_RE.test(data.email)){ setFieldError(f.email, "Escribe un correo válido."); ok=false; }
  if(data.password.length<8){ setFieldError(f.pass, "Usa al menos 8 caracteres."); ok=false; }
  else if(pass2!==undefined && $(pass2).value!==data.password){ setFieldError(f.pass2, "Las contraseñas no coinciden."); ok=false; }
  return ok;
}

const SELF_FIELDS = {nombre:"sr-f-nombre", email:"sr-f-email", pass:"sr-f-pass", pass2:"sr-f-pass2", nivel:"sr-f-nivel", interes:"sr-f-interes"};
bindRegister({
  formId:"registerFormEstudiante", role:"estudiante", fields:SELF_FIELDS, btnText:"Crear cuenta",
  call:(d)=> api.registrarEstudiante(d),
  read:()=> readStudentForm({nombre:"srNombre", email:"srEmail", pass:"srPass", nivel:"srNivel", interes:"srInteres"}, SELF_FIELDS, "srPass2")
});

const ADM_FIELDS = {nombre:"ar-f-nombre", email:"ar-f-email", pass:"ar-f-pass", pass2:"ar-f-pass2", codigo:"ar-f-codigo"};
bindRegister({
  formId:"registerFormAdmin", role:"admin", fields:ADM_FIELDS, btnText:"Crear cuenta de administrador",
  call:(d)=> api.registrarAdmin(d),
  read:()=>{
    const data = {nombre:$("arNombre").value.trim(), email:$("arEmail").value.trim(), password:$("arPass").value, codigoAutorizacion:$("arCodigo").value.trim()};
    let ok = checkCommon(ADM_FIELDS, null, data, "arPass2");
    if(!data.codigoAutorizacion){ setFieldError(ADM_FIELDS.codigo); ok=false; }
    return ok ? data : null;
  }
});

function logout(){
  state.token = null;
  state.user = null;
  state.cursos = []; state.todosCursos = [];
  saveSession();
  showLogin();
  $("loginFormEstudiante").reset();
  $("loginFormAdmin").reset();
}
$("logoutBtn").addEventListener("click", logout);


const NAV = {
  estudiante: [
    {panel:"catalogo", label:"Catálogo"},
    {panel:"consulta", label:"Consulta"},
    {panel:"historial", label:"Mi historial"}
  ],
  admin: [
    {panel:"catalogo", label:"Catálogo"},
    {panel:"estudiantes", label:"Estudiantes"},
    {panel:"admin", label:"Cursos"},
    {panel:"stats", label:"Estadísticas"}
  ]
};
const ALL_PANELS = ["catalogo","estudiantes","consulta","historial","admin","stats"];

function buildNav(){
  const nav = $("routeNav");
  nav.innerHTML = "";
  NAV[state.user.role].forEach(item=>{
    const btn = document.createElement("button");
    btn.className = "route-step";
    btn.dataset.panel = item.panel;
    btn.textContent = item.label;
    btn.addEventListener("click", ()=> showPanel(item.panel));
    nav.appendChild(btn);
  });
}

function showPanel(name){
  ALL_PANELS.forEach(p=> $("panel-"+p).classList.toggle("active", p===name));
  document.querySelectorAll(".route-step").forEach(btn=>{
    btn.classList.toggle("active", btn.dataset.panel===name);
  });
  $("hero").style.display = name==="catalogo" ? "" : "none";
  window.scrollTo({top:0, behavior:"smooth"});
  if(name==="historial") renderHistory();
  if(name==="estudiantes") renderStudents();
  if(name==="admin") refreshCourses();
  if(name==="stats") renderStats();
}

async function enterApp(){
  loginScreen.style.display = "none";
  appRoot.style.display = "block";
  const u = state.user;
  $("whoLabel").textContent = u.role==="estudiante" ? u.nombre+" · ID "+u.id : u.nombre;
  $("roleBadge").textContent = u.role==="admin" ? "Admin" : "Estudiante";
  $("roleBadge").className = "role-badge " + u.role;
  $("consultaWho").textContent = u.nombre + " (ID " + u.id + ")";
  buildNav();
  showPanel("catalogo");

  try{
    await loadCatalogos();
  }catch(err){ toast(err.message, true); }
  refreshCourses();
  loadStats().catch(()=>{  });
}


function fillCategoriaSelects(){
  fillSelect($("filterCategoria"), state.categorias, {placeholder:"Todas las categorías", value:(c)=>c.nombre});
  ["regInteres","srInteres"].forEach(id=> fillSelect($(id), state.categorias, {placeholder:"Selecciona…", value:(c)=>c.nombre}));
  fillSelect($("courseCat"), state.categorias);
}
async function loadPublicCategorias(){
  try{ state.categorias = await api.categorias(); fillCategoriaSelects(); }
  catch(e){  }
}
async function loadCatalogos(){
  [state.categorias, state.niveles] = await Promise.all([api.categorias(), api.niveles()]);
  fillCategoriaSelects();
  fillSelect($("filterNivel"), state.niveles, {placeholder:"Todos los niveles", value:(n)=>n.nombre});
  fillSelect($("courseNivel"), state.niveles);
}


async function refreshCourses(){
  $("courseGrid").innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando catálogo…</div>';
  try{
    const all = await api.cursos(isAdmin());
    state.todosCursos = all;
    state.cursos = all.filter(c=>c.estado);
    renderCatalog();
    if(isAdmin()) renderAdmin();
  }catch(e){
    $("courseGrid").innerHTML = '<div class="empty-state">No se pudo cargar el catálogo. '+escapeHtml(e.message)+'</div>';
  }
}

function renderCatalog(){
  const grid = $("courseGrid");
  const cat = $("filterCategoria").value;
  const niv = $("filterNivel").value;
  $("heroCourses").textContent = state.cursos.length;

  const courses = state.cursos.filter(c=> (!cat || c.categoria===cat) && (!niv || c.nivel===niv));
  if(courses.length===0){
    grid.innerHTML = '<div class="empty-state">No hay cursos activos que coincidan con estos filtros.</div>';
    return;
  }
  grid.innerHTML = courses.map(c=>`
    <div class="course-card">
      <span class="course-cat">${escapeHtml(c.categoria)}</span>
      <h3>${escapeHtml(c.nombre)}</h3>
      <p>${escapeHtml(c.descripcion)}</p>
      <div class="course-meta">
        <span class="pill">${escapeHtml(c.nivel)}</span>
        <span class="pill">${c.duracion} h</span>
      </div>
    </div>`).join("");
}
$("filterCategoria").addEventListener("change", renderCatalog);
$("filterNivel").addEventListener("change", renderCatalog);


const modalOverlay = $("modalOverlay");
const courseForm = $("courseForm");
const COURSE_FIELDS = ["cf-nombre","cf-desc","cf-cat","cf-nivel","cf-dur"];

function openCourseModal(course){
  $("modalTitle").textContent = course ? "Editar curso" : "Nuevo curso";
  $("courseId").value = course ? course.id : "";
  $("courseNombre").value = course ? course.nombre : "";
  $("courseDesc").value = course ? course.descripcion : "";
  $("courseCat").value = course ? course.categoriaId : (state.categorias[0]?.id ?? "");
  $("courseNivel").value = course ? course.nivelId : (state.niveles[0]?.id ?? "");
  $("courseDur").value = course ? course.duracion : "";
  $("courseActivo").value = course ? String(course.estado) : "true";
  COURSE_FIELDS.forEach(clearFieldError);
  modalOverlay.classList.add("show");
}
const closeModal = ()=> modalOverlay.classList.remove("show");
$("newCourseBtn").addEventListener("click", ()=> openCourseModal(null));
$("modalClose").addEventListener("click", closeModal);
modalOverlay.addEventListener("click", (e)=>{ if(e.target===modalOverlay) closeModal(); });

courseForm.addEventListener("submit", async (e)=>{
  e.preventDefault();
  COURSE_FIELDS.forEach(clearFieldError);
  const id = $("courseId").value;
  const data = {
    nombre: $("courseNombre").value.trim(),
    descripcion: $("courseDesc").value.trim(),
    categoriaId: $("courseCat").value,
    nivelId: $("courseNivel").value,
    duracion: Number($("courseDur").value),
    estado: $("courseActivo").value==="true"
  };

  let valid = true;
  if(!data.nombre){ setFieldError("cf-nombre"); valid=false; }
  if(!data.descripcion){ setFieldError("cf-desc"); valid=false; }
  if(!data.categoriaId){ setFieldError("cf-cat"); valid=false; }
  if(!data.nivelId){ setFieldError("cf-nivel"); valid=false; }
  if(!(data.duracion>0)){ setFieldError("cf-dur"); valid=false; }
  if(!valid) return;

  try{
    await api.guardarCurso(id || null, data);
    toast(id ? "Curso actualizado." : "Curso creado.");
    closeModal();
    refreshCourses();
  }catch(err){
    toast(err.message, true);
  }
});

function renderAdmin(){
  const tbody = $("adminTbody");
  const all = state.todosCursos;
  $("adminCount").textContent = all.length + " curso(s) en el catálogo";
  tbody.innerHTML = "";
  all.forEach(c=>{
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td><strong>${escapeHtml(c.nombre)}</strong><div style="color:var(--text-faint);font-size:.78rem;max-width:280px">${escapeHtml(c.descripcion.slice(0,90))}${c.descripcion.length>90?"…":""}</div></td>
      <td>${escapeHtml(c.categoria)}</td>
      <td>${escapeHtml(c.nivel)}</td>
      <td>${c.duracion} h</td>
      <td><span class="state-dot ${c.estado?'on':'off'}"></span>${c.estado?'Activo':'Inactivo'}</td>
      <td><div class="admin-actions">
        <button class="btn btn-ghost btn-sm edit-btn">Editar</button>
        ${c.estado?'<button class="btn btn-danger btn-sm deact-btn">Desactivar</button>':''}
      </div></td>`;
    tr.querySelector(".edit-btn").addEventListener("click", ()=> openCourseModal(c));
    const deact = tr.querySelector(".deact-btn");
    if(deact) deact.addEventListener("click", async ()=>{
      try{ await api.desactivarCurso(c.id); toast("Curso desactivado."); refreshCourses(); }
      catch(err){ toast(err.message, true); }
    });
    tbody.appendChild(tr);
  });
}


const registroForm = $("registroForm");
const ADMIN_STUDENT_FIELDS = {nombre:"f-nombre", email:"f-email", pass:"f-pass", nivel:"f-nivel", interes:"f-interes"};
const ADMIN_STUDENT_INPUTS = {nombre:"regNombre", email:"regEmail", pass:"regPass", nivel:"regNivel", interes:"regInteres"};
function readStudentForm(inputs, fields, pass2){
  Object.values(fields).forEach(clearFieldError);
  const data = {
    nombre: $(inputs.nombre).value.trim(),
    email: $(inputs.email).value.trim(),
    password: $(inputs.pass).value,
    nivelExperiencia: $(inputs.nivel).value,
    areaInteres: $(inputs.interes).value
  };
  let ok = checkCommon(fields, inputs, data, pass2);
  if(!data.nivelExperiencia){ setFieldError(fields.nivel); ok=false; }
  if(!data.areaInteres){ setFieldError(fields.interes); ok=false; }
  return ok ? data : null;
}

registroForm.addEventListener("submit", async (e)=>{
  e.preventDefault();
  const data = readStudentForm(ADMIN_STUDENT_INPUTS, ADMIN_STUDENT_FIELDS);
  if(!data) return;

  const btn = registroForm.querySelector("button[type=submit]");
  btn.disabled = true; btn.textContent = "Creando…";
  try{
    const student = await api.crearEstudiante(data);
    toast("Cuenta creada para "+student.nombre+" (ID: "+student.id+"). Ya puede iniciar sesión.");
    $("newStudentInfo").style.display = "block";
    $("newStudentIdValue").textContent = student.id;
    registroForm.reset();
    renderStudents();
  }catch(err){
    setFieldError("f-email", err.message);
    toast(err.message, true);
  }finally{
    btn.disabled = false; btn.textContent = "Crear cuenta de estudiante";
  }
});

async function renderStudents(){
  const tbody = $("studentsTbody");
  tbody.innerHTML = `<tr><td colspan="5"><div class="loading-row"><span class="spinner"></span> Cargando…</div></td></tr>`;
  try{
    const students = await api.estudiantes();
    if(students.length===0){
      tbody.innerHTML = `<tr><td colspan="5" style="color:var(--text-faint)">Aún no hay estudiantes registrados.</td></tr>`;
      return;
    }
    tbody.innerHTML = "";
    students.forEach(s=>{
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td><span class="pill" style="font-family:'Space Grotesk';font-weight:600">${escapeHtml(s.id)}</span></td>
        <td><strong>${escapeHtml(s.nombre)}</strong><div style="color:var(--text-faint);font-size:.78rem">${escapeHtml(s.email)}</div></td>
        <td>${escapeHtml(s.nivelExperiencia)}</td>
        <td>${escapeHtml(s.areaInteres||"—")}</td>
        <td><button class="btn btn-danger btn-sm del-student-btn">Eliminar</button></td>`;
      tr.querySelector(".del-student-btn").addEventListener("click", async ()=>{
        if(!confirm(`¿Eliminar la cuenta de ${s.nombre} (ID: ${s.id})? Esta acción no se puede deshacer y también borrará su historial de consultas.`)) return;
        try{
          await api.eliminarEstudiante(s.id);
          toast("Cuenta de "+s.nombre+" eliminada.");
          renderStudents();
        }catch(err){ toast(err.message, true); }
      });
      tbody.appendChild(tr);
    });
  }catch(e){
    tbody.innerHTML = `<tr><td colspan="5">No se pudo cargar la lista. ${escapeHtml(e.message)}</td></tr>`;
  }
}


EXAMPLES.forEach(ex=>{
  const chip = document.createElement("span");
  chip.className = "example-chip";
  chip.textContent = ex;
  chip.addEventListener("click", ()=>{ $("consultaTexto").value = ex; });
  $("examples").appendChild(chip);
});

$("consultaSubmit").addEventListener("click", async ()=>{
  const field = $("consultaTexto").closest(".field");
  const texto = $("consultaTexto").value.trim();
  field.classList.remove("has-error");
  if(!texto){ field.classList.add("has-error"); return; }

  const btn = $("consultaSubmit");
  $("resultCard").style.display = "none";
  $("consultaLoading").style.display = "flex";
  btn.disabled = true;
  try{
    renderResult(await api.consultar(texto));
  }catch(err){
    renderResult({estado:"Error", pregunta:texto, recomendacion:null, errorMsg:err.message});
  }finally{
    $("consultaLoading").style.display = "none";
    btn.disabled = false;
  }
});
function sourcesHtml(fuentes, compact){
  return fuentes.map(f=>`<div class="source-item">
    <div><h4${compact?' style="font-size:.88rem"':''}>${escapeHtml(f.nombre)}</h4><p>${escapeHtml(f.categoria)} · ${compact?"":"nivel "}${escapeHtml(f.nivel)}</p></div>
    <div class="similarity">${Math.round(f.similitud*100)}%</div>
  </div>`).join("");
}

function renderResult(c){
  const card = $("resultCard");
  card.style.display = "block";
  const rec = c.recomendacion;
  let body = `<span class="result-status ${statusClass(c.estado)}">${escapeHtml(c.estado)}</span>`;

  if(c.estado==="Respondida" && rec){
    body += `<div class="result-answer">${escapeHtml(rec.contenido)}</div>`;
    body += `<div class="sources-head">Fuentes utilizadas (${rec.fuentes.length})</div>`;
    body += sourcesHtml(rec.fuentes, false);
    body += ratingBlockHtml();
  }else if(c.estado==="Sin resultados"){
    body += `<p style="color:var(--text-muted)">Ningún curso del catálogo fue lo bastante relevante para esta pregunta. No se generó una respuesta para evitar recomendar cursos que no existen.</p>`;
  }else{
    body += `<p style="color:var(--text-muted)">Ocurrió un error al procesar la consulta${c.errorMsg? ": "+escapeHtml(c.errorMsg):""}. Intenta nuevamente.</p>`;
  }
  card.innerHTML = body;
  if(c.estado==="Respondida" && rec) attachRatingHandlers(rec.id, card);
}

function ratingBlockHtml(){
  return `<div class="rating-block">
    <label>Califica esta recomendación</label>
    <div class="stars">${[1,2,3,4,5].map(n=>`<button type="button" class="star" data-n="${n}">${n}</button>`).join("")}</div>
    <textarea placeholder="Comentario opcional" class="rating-comment" style="min-height:60px"></textarea>
    <button class="btn btn-ghost btn-sm rating-submit" style="margin-top:10px">Enviar calificación</button>
  </div>`;
}
function attachRatingHandlers(recomendacionId, scope){
  let selected = 0;
  const stars = scope.querySelectorAll(".star");
  stars.forEach(st=>{
    st.addEventListener("click", ()=>{
      selected = Number(st.dataset.n);
      stars.forEach(s=> s.classList.toggle("on", Number(s.dataset.n)<=selected));
    });
  });
  scope.querySelector(".rating-submit").addEventListener("click", async (e)=>{
    if(!selected){ toast("Selecciona una puntuación entre 1 y 5.", true); return; }
    const comentario = scope.querySelector(".rating-comment").value.trim();
    e.target.disabled = true;
    try{
      await api.calificar(recomendacionId, selected, comentario);
      toast("Gracias por tu calificación.");
      scope.querySelector(".rating-block").innerHTML = "<p style='color:var(--text-muted);font-size:.85rem'>Calificación registrada. ¡Gracias!</p>";
    }catch(err){
      toast(err.message, true);
      e.target.disabled = false;
    }
  });
}


async function renderHistory(){
  const list = $("historyList");
  list.innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando historial…</div>';
  try{
    const items = await api.historial(state.user.id);
    if(items.length===0){ list.innerHTML = '<div class="empty-state">Aún no has hecho consultas.</div>'; return; }
    list.innerHTML = "";
    items.forEach(q=>{
      const rec = q.recomendacion;
      const answered = q.estado==="Respondida" && rec;
      const resumen = answered ? (rec.fuentes.length + " curso(s) recomendado(s)") : q.estado;
      const el = document.createElement("div");
      el.className = "history-item";
      el.innerHTML = `
        <div class="history-top">
          <span class="history-q">${escapeHtml(q.pregunta)}</span>
          <span class="result-status ${statusClass(q.estado)}" style="margin:0">${escapeHtml(q.estado)}</span>
        </div>
        <div class="history-date">${new Date(q.fecha).toLocaleString()} · ${escapeHtml(resumen)}</div>
        <div class="history-detail"></div>`;
      el.addEventListener("click", ()=>{
        const wasOpen = el.classList.contains("open");
        document.querySelectorAll(".history-item.open").forEach(o=>o.classList.remove("open"));
        if(wasOpen) return;
        el.classList.add("open");
        const detail = el.querySelector(".history-detail");
        if(answered){
          const cal = rec.calificacion;
          detail.innerHTML = `<div class="result-answer" style="font-size:.9rem">${escapeHtml(rec.contenido)}</div>
            <div class="sources-head">Fuentes</div>` + sourcesHtml(rec.fuentes, true) +
            (cal ? `<p style="margin-top:12px;font-size:.85rem;color:var(--text-muted)">Tu calificación: ${"★".repeat(cal.puntuacion)}${cal.comentario? " — "+escapeHtml(cal.comentario):""}</p>` : "");
        }else{
          detail.innerHTML = `<p style="font-size:.85rem;color:var(--text-muted)">Estado: ${escapeHtml(q.estado)}</p>`;
        }
      });
      list.appendChild(el);
    });
  }catch(e){
    list.innerHTML = '<div class="empty-state">No se pudo cargar el historial. '+escapeHtml(e.message)+'</div>';
  }
}


async function loadStats(){
  const s = await api.estadisticas();
  $("heroAnswered").textContent = s.respondidas;
  $("heroAvgRating").textContent = fmtRating(s.promedioCalificacion);
  return s;
}
async function renderStats(){
  const grid = $("statGrid");
  grid.innerHTML = '<div class="loading-row"><span class="spinner"></span> Calculando…</div>';
  try{
    const s = await loadStats();
    grid.innerHTML = `
      <div class="stat-card"><b>${s.total}</b><span>consultas totales</span></div>
      <div class="stat-card"><b>${s.respondidas}</b><span>respondidas</span></div>
      <div class="stat-card"><b>${s.sinResultados}</b><span>sin resultados</span></div>
      <div class="stat-card"><b>${fmtRating(s.promedioCalificacion)}</b><span>calificación promedio</span></div>`;
    $("topCourse").textContent = s.cursoMasRecomendado || "Aún no hay suficientes datos.";
  }catch(e){
    grid.innerHTML = '<div class="empty-state">No se pudieron cargar las estadísticas.</div>';
  }
}


const saved = loadSession();
if(saved && saved.token){
  state.token = saved.token;
  loginScreen.style.display = "none";
  api.me()
    .then(u=>{ state.user = normalizeUser(u); saveSession(); enterApp(); })
    .catch(()=>{ if(state.token) logout(); });
}else{
  showLogin();
  loadPublicCategorias();
}

})();