(function(){
"use strict";

const API_BASE = (window.RUTAIA_API_BASE || "/api").replace(/\/$/, "");
const SESSION_KEY = "rutaia_session";
const ROLES = {ESTUDIANTE:"estudiante", ADMINISTRADOR:"admin"};

const EXAMPLES = [
  "Desarrollo web con React y TypeScript",
  "Backend con Java y Spring Boot",
  "Inteligencia Artificial y Machine Learning",
  "Análisis de datos con SQL y Python",
  "Ciberseguridad y pentesting",
  "Microservicios con Docker",
  "Bases de datos con PostgreSQL y Qdrant"
];

const DEFAULT_CATEGORIAS = [
  {id:"cat-001", nombre:"Desarrollo Web"},
  {id:"cat-002", nombre:"Bases de Datos"},
  {id:"cat-003", nombre:"Inteligencia Artificial"},
  {id:"cat-004", nombre:"Automatización"},
  {id:"cat-005", nombre:"Análisis de Datos"},
  {id:"cat-006", nombre:"Seguridad Informática"},
  {id:"cat-007", nombre:"DevOps"}
];

const state = {
  token: null,
  user: null,
  categorias: [...DEFAULT_CATEGORIAS],
  niveles: [],
  cursos: [],
  todosCursos: [],
  selectedCourseForModal: null
};

const $ = (id) => document.getElementById(id);
const isAdmin = () => !!state.user && state.user.role === ROLES.ADMINISTRADOR;

/* -------------------------------------------------------------
 * Security & Helpers
 * ------------------------------------------------------------- */
function escapeHtml(str){
  return String(str ?? "").replace(/[&<>"']/g, m=>({
    "&":"&amp;",
    "<":"&lt;",
    ">":"&gt;",
    '"':"&quot;",
    "'":"&#39;"
  }[m]));
}

function maskUuid(uuid){
  if(!uuid) return "—";
  const str = String(uuid).trim();
  if(str.length <= 10) return str;
  return str.slice(0, 6) + "••••" + str.slice(-4);
}

async function copyToClipboard(text, successMsg = "Copiado al portapapeles."){
  try{
    if(navigator.clipboard && navigator.clipboard.writeText){
      await navigator.clipboard.writeText(text);
    }else{
      const textarea = document.createElement("textarea");
      textarea.value = text;
      textarea.style.position = "fixed";
      textarea.style.opacity = "0";
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand("copy");
      document.body.removeChild(textarea);
    }
    toast(successMsg);
  }catch(e){
    toast("No se pudo copiar.", true);
  }
}

function renderSafeMarkdown(rawText){
  if(!rawText) return "";
  const escaped = escapeHtml(rawText);
  const lines = escaped.split(/\r?\n/);
  let html = "";
  let inList = false;

  for(let i = 0; i < lines.length; i++){
    let line = lines[i].trim();

    if(!line){
      if(inList){ html += "</ul>"; inList = false; }
      continue;
    }

    if(/^###\s+(.+)$/.test(line) || /^##\s+(.+)$/.test(line)){
      if(inList){ html += "</ul>"; inList = false; }
      const title = line.replace(/^#{2,3}\s+/, "");
      html += `<h4>${formatInlineMarkdown(title)}</h4>`;
      continue;
    }

    if(/^[-*•]\s+(.+)$/.test(line)){
      if(!inList){ html += "<ul>"; inList = true; }
      const item = line.replace(/^[-*•]\s+/, "");
      html += `<li>${formatInlineMarkdown(item)}</li>`;
      continue;
    }

    if(inList){ html += "</ul>"; inList = false; }
    html += `<p>${formatInlineMarkdown(line)}</p>`;
  }

  if(inList){ html += "</ul>"; }
  return html;
}

function formatInlineMarkdown(str){
  return str
    .replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>")
    .replace(/\*([^*]+)\*/g, "<em>$1</em>")
    .replace(/`([^`]+)`/g, "<code>$1</code>");
}

function parseCourseDescription(desc){
  const text = String(desc || "");
  const parts = { prerrequisitos: "", temario: "", herramientas: "", resumen: "" };

  const preMatch = text.match(/Prerrequisitos:\s*([^.]*\.)/i);
  if(preMatch) parts.prerrequisitos = preMatch[1].trim();

  const temMatch = text.match(/Temario:\s*(.*?)(?=Herramientas:|Al finalizar:|Al completar:|$)/i);
  if(temMatch) parts.temario = temMatch[1].trim();

  const herMatch = text.match(/Herramientas:\s*(.*?)(?=Al finalizar:|Al completar:|$)/i);
  if(herMatch) parts.herramientas = herMatch[1].trim();

  const finMatch = text.match(/(?:Al finalizar|Al completar)[,:]?\s*(.*)$/i);
  if(finMatch){
    parts.resumen = finMatch[1].trim();
  }else{
    const firstSentence = text.split(/[.!?]/)[0];
    parts.resumen = firstSentence ? firstSentence.trim() + "." : text.slice(0, 130) + "...";
  }

  return parts;
}

/* -------------------------------------------------------------
 * UI Feedback & Inputs
 * ------------------------------------------------------------- */
let toastTimer = null;
function toast(msg, isErr){
  const el = $("toast");
  el.textContent = msg;
  el.className = "toast show" + (isErr ? " err" : "");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(()=>{ el.classList.remove("show"); }, 3000);
}

const clearFieldError = (id) => {
  const field = $(id);
  if(field) field.classList.remove("has-error");
};

const setFieldError = (id, msg) => {
  const f = $(id);
  if(!f) return;
  f.classList.add("has-error");
  if(msg){
    const t = f.querySelector(".error-text");
    if(t) t.textContent = msg;
  }
};

function statusClass(estado){
  const clean = String(estado || "").toLowerCase().replace(/[\s_-]+/g, "");
  if(clean.includes("respondida")) return "status-Respondida";
  if(clean.includes("sinresultado")) return "status-Sinresultados";
  if(clean.includes("error")) return "status-Error";
  return "status-Pendiente";
}

function fillSelect(el, items, {placeholder, value=(i)=>i.id, label=(i)=>i.nombre}={}){
  if(!el || !Array.isArray(items)) return;
  const prev = el.value;
  el.innerHTML = (placeholder !== undefined ? `<option value="">${escapeHtml(placeholder)}</option>` : "") +
    items.map(i => `<option value="${escapeHtml(value(i))}">${escapeHtml(label(i))}</option>`).join("");
  if(prev) el.value = prev;
}

const fmtRating = (n) => (n === null || n === undefined || isNaN(n)) ? "—" : Number(n).toFixed(1);

function getUserInitials(name){
  if(!name) return "U";
  const parts = name.trim().split(/\s+/);
  if(parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
  return parts[0].slice(0, 2).toUpperCase();
}

function normalizeUser(u){
  const source = u?.usuario || u?.user || u;
  const role = String(source?.rol || source?.role || "").toUpperCase();
  const normalizedRole = (role === "ADMIN" || role === "ADMINISTRADOR") ? ROLES.ADMINISTRADOR :
    (role === "ESTUDIANTE" ? ROLES.ESTUDIANTE : null);
  if(!source?.id || !normalizedRole) throw new Error("Respuesta de autenticación no válida.");
  return {
    id: source.id,
    nombre: source.nombre || source.name || source.email || source.id,
    email: source.email || "",
    role: normalizedRole
  };
}

/* -------------------------------------------------------------
 * Session Storage
 * ------------------------------------------------------------- */
function saveSession(){
  try{
    if(state.token){
      localStorage.setItem(SESSION_KEY, JSON.stringify({token: state.token, user: state.user}));
    } else {
      localStorage.removeItem(SESSION_KEY);
    }
  }catch(e){
    toast("No se pudo guardar la sesión.", true);
  }
}

function loadSession(){
  try{ return JSON.parse(localStorage.getItem(SESSION_KEY)); }catch(e){ return null; }
}

/* -------------------------------------------------------------
 * HTTP & API Client
 * ------------------------------------------------------------- */
async function http(path, {method="GET", body, auth=true}={}){
  const headers = {};
  if(body !== undefined) headers["Content-Type"] = "application/json";
  if(auth && state.token) headers["Authorization"] = "Bearer " + state.token;

  let res;
  try{
    res = await fetch(API_BASE + path, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined
    });
  }catch(e){
    throw new Error("No se pudo conectar con el servidor.");
  }

  if(res.status === 401 && auth){
    logout();
    throw new Error("Sesión expirada. Ingresa de nuevo.");
  }

  if(!res.ok){
    let msg = res.status === 401 ? "Credenciales incorrectas." : `Error (${res.status})`;
    try{
      const b = await res.json();
      if(b.message || b.error) msg = b.message || b.error;
    }catch(e){}
    throw new Error(msg);
  }

  return res.status === 204 ? null : res.json();
}

const enc = encodeURIComponent;

const api = {
  login: (identificador, password) => http("/auth/login", {method:"POST", body:{identificador, password}, auth:false}),
  registrarEstudiante: (data) => http("/auth/register/estudiante", {method:"POST", body:data, auth:false}),
  registrarAdmin: (data) => http("/auth/register/admin", {method:"POST", body:data, auth:false}),
  me: () => http("/auth/me"),
  categorias: () => http("/categorias", {auth:false}),
  niveles: () => http("/niveles-dificultad", {auth:false}),
  cursos: (incluirInactivos) => http("/cursos" + (incluirInactivos ? "?incluirInactivos=true" : "")),
  guardarCurso: (id, data) => id ? http("/cursos/" + enc(id), {method:"PUT", body:data}) : http("/cursos", {method:"POST", body:data}),
  desactivarCurso: (id) => http("/cursos/" + enc(id) + "/desactivar", {method:"PATCH"}),
  estudiantes: () => http("/estudiantes"),
  crearEstudiante: (data) => http("/estudiantes", {method:"POST", body:data}),
  eliminarEstudiante: (id) => http("/estudiantes/" + enc(id), {method:"DELETE"}),
  consultar: (pregunta) => http("/consultas", {method:"POST", body:{estudianteId: state.user?.id, pregunta}}),
  historial: (estudianteId) => http("/consultas/historial/" + enc(estudianteId)),
  calificar: (recomendacionId, puntuacion, comentario) =>
    http("/calificaciones", {method:"POST", body:{estudianteId: state.user?.id, recomendacionId, puntuacion, comentario}}),
  estadisticas: () => http("/estadisticas")
};

/* -------------------------------------------------------------
 * Auth & Login Screens
 * ------------------------------------------------------------- */
const loginScreen = $("loginScreen");
const appRoot = $("appRoot");

function showLogin(){
  loginScreen.style.display = "flex";
  appRoot.style.display = "none";
}

const AUTH_FORMS = {
  "estudiante-login": "loginFormEstudiante",
  "estudiante-register": "registerFormEstudiante",
  "admin-login": "loginFormAdmin"
};
let authRole = "estudiante", authMode = "login";

function showAuthForm(){
  if(authRole === "admin") authMode = "login";
  const active = AUTH_FORMS[authRole + "-" + authMode] || AUTH_FORMS["estudiante-login"];
  Object.values(AUTH_FORMS).forEach(id=>{
    const el = $(id);
    if(el) el.classList.toggle("active", id === active);
  });
  if(authMode === "register" && authRole === "estudiante"){
    fillCategoriaSelects();
    loadPublicCategorias();
  }
}

document.querySelectorAll(".role-toggle button").forEach(btn=>{
  btn.addEventListener("click", ()=>{
    document.querySelectorAll(".role-toggle button").forEach(b=>b.classList.remove("active"));
    btn.classList.add("active");
    authRole = btn.dataset.role;
    if(authRole === "admin") authMode = "login";
    showAuthForm();
  });
});

document.querySelectorAll("[data-auth]").forEach(link=>{
  link.addEventListener("click", (e)=>{
    e.preventDefault();
    authMode = link.dataset.auth;
    if(authRole === "admin" && authMode === "register"){
      authRole = "estudiante";
      document.querySelectorAll(".role-toggle button").forEach(b=>b.classList.toggle("active", b.dataset.role === "estudiante"));
    }
    showAuthForm();
  });
});

function completeAuth(res, role){
  const token = res?.token || res?.accessToken;
  if(!token) throw new Error("Sesión no válida.");
  const user = normalizeUser(res);
  if(user.role !== role){
    throw new Error("Estas credenciales no corresponden a este tipo de cuenta.");
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
    if(!identificador){ setFieldError(fieldId, "Ingresa tu correo."); return; }
    if(!password){ setFieldError(fieldId, "Ingresa tu contraseña."); return; }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Ingresando…";
    try{
      completeAuth(await api.login(identificador, password.trim()), role);
    }catch(err){
      setFieldError(fieldId, err.message);
    }finally{
      btn.disabled = false;
      btn.textContent = "Ingresar";
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
    btn.disabled = true;
    btn.textContent = "Creando…";
    try{
      const response = await call(data);
      form.reset();
      if(response?.token || response?.accessToken){
        completeAuth(response, role);
      }else{
        authMode = "login";
        showAuthForm();
        toast("Cuenta creada. Ya puedes ingresar.");
      }
    }catch(err){
      setFieldError(fields.email, err.message);
    }finally{
      btn.disabled = false;
      btn.textContent = btnText;
    }
  });
}

function checkCommon(f, i, data, pass2){
  let ok = true;
  if(data.nombre.length < 2){ setFieldError(f.nombre, "Nombre requerido."); ok = false; }
  if(!EMAIL_RE.test(data.email)){ setFieldError(f.email, "Correo inválido."); ok = false; }
  if(data.password.length < 8){ setFieldError(f.pass, "Mínimo 8 caracteres."); ok = false; }
  else if(pass2 !== undefined && $(pass2).value !== data.password){ setFieldError(f.pass2, "No coinciden."); ok = false; }
  return ok;
}

const SELF_FIELDS = {nombre:"sr-f-nombre", email:"sr-f-email", pass:"sr-f-pass", pass2:"sr-f-pass2", nivel:"sr-f-nivel", interes:"sr-f-interes"};
bindRegister({
  formId:"registerFormEstudiante", role:"estudiante", fields:SELF_FIELDS, btnText:"Crear cuenta",
  call:(d)=> api.registrarEstudiante(d),
  read:()=> readStudentForm({nombre:"srNombre", email:"srEmail", pass:"srPass", nivel:"srNivel", interes:"srInteres"}, SELF_FIELDS, "srPass2")
});

function logout(){
  state.token = null;
  state.user = null;
  state.cursos = [];
  state.todosCursos = [];
  saveSession();
  showLogin();
  closeAllModals();
  $("loginFormEstudiante").reset();
  $("loginFormAdmin").reset();
}
$("logoutBtn").addEventListener("click", logout);

/* -------------------------------------------------------------
 * Navigation & Panels
 * ------------------------------------------------------------- */
const NAV = {
  estudiante: [
    {panel:"catalogo", label:"Catálogo", icon:"📖"},
    {panel:"consulta", label:"Consulta", icon:"✦"},
    {panel:"historial", label:"Historial", icon:"📜"}
  ],
  admin: [
    {panel:"catalogo", label:"Catálogo", icon:"📖"},
    {panel:"consulta", label:"Consulta", icon:"✦"},
    {panel:"historial", label:"Historial", icon:"📜"},
    {panel:"estudiantes", label:"Estudiantes", icon:"👥"},
    {panel:"admin", label:"Cursos", icon:"⚙️"},
    {panel:"stats", label:"Estadísticas", icon:"📊"}
  ]
};
const ALL_PANELS = ["catalogo", "estudiantes", "consulta", "historial", "admin", "stats"];

function buildNav(){
  const nav = $("routeNav");
  nav.innerHTML = "";
  NAV[state.user.role].forEach(item=>{
    const btn = document.createElement("button");
    btn.className = "route-step";
    btn.dataset.panel = item.panel;
    btn.innerHTML = `<span class="route-step-icon">${item.icon}</span> <span>${item.label}</span>`;
    btn.addEventListener("click", ()=> showPanel(item.panel));
    nav.appendChild(btn);
  });
}

function showPanel(name){
  ALL_PANELS.forEach(p=> {
    const el = $("panel-" + p);
    if(el) el.classList.toggle("active", p === name);
  });
  document.querySelectorAll(".route-step").forEach(btn=>{
    btn.classList.toggle("active", btn.dataset.panel === name);
  });
  $("hero").style.display = (name === "catalogo") ? "" : "none";
  window.scrollTo({top:0, behavior:"smooth"});
  if(name === "historial") renderHistory();
  if(name === "estudiantes") renderStudents();
  if(name === "admin") refreshCourses();
  if(name === "stats") renderStats();
}

function setupUserProfile(){
  const u = state.user;
  if(!u) return;

  $("whoLabel").textContent = u.nombre;
  $("userAvatar").textContent = getUserInitials(u.nombre);
  $("roleBadge").textContent = u.role === ROLES.ADMINISTRADOR ? "Admin" : "Estudiante";
  $("roleBadge").className = "role-badge " + u.role;

  $("consultaWho").textContent = u.nombre;

  $("profileAvatarBig").textContent = getUserInitials(u.nombre);
  $("profileNameVal").textContent = u.nombre;
  $("profileEmailVal").textContent = u.email || "—";
  $("profileRoleVal").textContent = u.role === ROLES.ADMINISTRADOR ? "Administrador" : "Estudiante";
  $("profileMaskedId").textContent = maskUuid(u.id);

  $("profileMaskedId").onclick = () => copyToClipboard(u.id, "ID copiado.");
  $("profileCopyIdBtn").onclick = () => copyToClipboard(u.id, "ID copiado.");
}

$("userProfileBtn").addEventListener("click", ()=>{
  $("userProfileModalOverlay").classList.add("show");
});
$("profileModalClose").addEventListener("click", ()=>{
  $("userProfileModalOverlay").classList.remove("show");
});
$("userProfileModalOverlay").addEventListener("click", (e)=>{
  if(e.target === $("userProfileModalOverlay")) $("userProfileModalOverlay").classList.remove("show");
});

$("brandHomeBtn").addEventListener("click", ()=> showPanel("catalogo"));
$("heroCtaBtn").addEventListener("click", ()=> showPanel("consulta"));
$("heroCatalogBtn").addEventListener("click", ()=>{
  $("panel-catalogo").scrollIntoView({behavior:"smooth"});
});

async function enterApp(){
  loginScreen.style.display = "none";
  appRoot.style.display = "block";
  setupUserProfile();
  buildNav();
  showPanel("catalogo");

  try{
    await loadCatalogos();
  }catch(err){
    toast(err.message, true);
  }
  refreshCourses();
  loadStats().catch(()=>{});
}

/* -------------------------------------------------------------
 * Catalog & Course Exploration
 * ------------------------------------------------------------- */
function fillCategoriaSelects(){
  const list = (state.categorias && state.categorias.length) ? state.categorias : DEFAULT_CATEGORIAS;
  const filterCat = $("filterCategoria");
  if(filterCat) fillSelect(filterCat, list, {placeholder:"Todas las categorías", value:(c)=>c.nombre});
  ["regInteres", "srInteres"].forEach(id=>{
    const el = $(id);
    if(el) fillSelect(el, list, {placeholder:"Selecciona…", value:(c)=>c.nombre});
  });
  const courseCat = $("courseCat");
  if(courseCat) fillSelect(courseCat, list);
}

async function loadPublicCategorias(){
  try{
    const res = await api.categorias();
    if(Array.isArray(res) && res.length > 0){
      state.categorias = res;
    }
  }catch(e){
    if(!state.categorias || !state.categorias.length){
      state.categorias = [...DEFAULT_CATEGORIAS];
    }
  }finally{
    fillCategoriaSelects();
  }
}

async function loadCatalogos(){
  try{
    [state.categorias, state.niveles] = await Promise.all([api.categorias(), api.niveles()]);
  }catch(e){
    if(!state.categorias || !state.categorias.length){
      state.categorias = [...DEFAULT_CATEGORIAS];
    }
  }
  fillCategoriaSelects();
  fillSelect($("filterNivel"), state.niveles, {placeholder:"Todos los niveles", value:(n)=>n.nombre});
  fillSelect($("courseNivel"), state.niveles);
}

async function refreshCourses(){
  $("courseGrid").innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando cursos…</div>';
  try{
    const all = await api.cursos(isAdmin());
    state.todosCursos = all;
    state.cursos = all.filter(c => c.estado);
    renderCatalog();
    if(isAdmin()) renderAdmin();
  }catch(e){
    $("courseGrid").innerHTML = `<div class="empty-state"><h3>No se pudo cargar el catálogo</h3><p>${escapeHtml(e.message)}</p></div>`;
  }
}

function getLevelBadgeClass(nivel){
  const n = String(nivel || "").toLowerCase();
  if(n.includes("básico") || n.includes("basico")) return "level-basico";
  if(n.includes("intermedio")) return "level-intermedio";
  return "level-avanzado";
}

function renderCatalog(){
  const grid = $("courseGrid");
  const cat = $("filterCategoria").value;
  const niv = $("filterNivel").value;
  const search = ($("filterSearch").value || "").trim().toLowerCase();

  $("heroCourses").textContent = state.cursos.length;

  const courses = state.cursos.filter(c => {
    const matchCat = !cat || c.categoria === cat;
    const matchNiv = !niv || c.nivel === niv;
    const matchSearch = !search ||
      c.nombre.toLowerCase().includes(search) ||
      c.descripcion.toLowerCase().includes(search) ||
      (c.categoria && c.categoria.toLowerCase().includes(search));
    return matchCat && matchNiv && matchSearch;
  });

  const clearBtn = $("clearFiltersBtn");
  if(clearBtn){
    clearBtn.style.display = (cat || niv || search) ? "inline-block" : "none";
  }

  $("catalogCount").textContent = `${courses.length} de ${state.cursos.length} cursos`;

  if(courses.length === 0){
    grid.innerHTML = `
      <div class="empty-state" style="grid-column: 1 / -1">
        <h3>Sin resultados</h3>
        <p>Prueba con otros términos o limpia los filtros.</p>
      </div>`;
    return;
  }

  grid.innerHTML = "";
  courses.forEach(c => {
    const parsed = parseCourseDescription(c.descripcion);
    const card = document.createElement("div");
    card.className = "course-card";
    card.innerHTML = `
      <div class="course-card-top">
        <span class="course-cat">${escapeHtml(c.categoria)}</span>
        <span class="course-level-badge ${getLevelBadgeClass(c.nivel)}">${escapeHtml(c.nivel)}</span>
      </div>
      <h3>${escapeHtml(c.nombre)}</h3>
      <p class="course-summary">${escapeHtml(parsed.resumen || c.descripcion.slice(0, 130) + "...")}</p>
      ${parsed.herramientas ? `
        <div class="course-highlights">
          <div class="course-highlights-title">Herramientas</div>
          <div class="course-highlights-text">${escapeHtml(parsed.herramientas)}</div>
        </div>
      ` : ''}
      <div class="course-card-meta">
        <div class="meta-left">
          <span class="pill">⏱️ ${escapeHtml(c.duracion)} h</span>
        </div>
      </div>
      <div class="course-card-actions">
        <button class="btn btn-ghost btn-sm view-details-btn">Temario</button>
        <button class="btn btn-primary btn-sm ask-ai-btn">✦ Consultar</button>
      </div>
    `;

    card.querySelector(".view-details-btn").addEventListener("click", () => openCourseDetailModal(c));
    card.querySelector(".ask-ai-btn").addEventListener("click", () => {
      showPanel("consulta");
      $("consultaTexto").value = `¿Qué prerrequisitos y contenidos tiene el curso "${c.nombre}"?`;
      updateCharCounter();
      $("consultaTexto").focus();
    });

    grid.appendChild(card);
  });
}

$("filterCategoria").addEventListener("change", renderCatalog);
$("filterNivel").addEventListener("change", renderCatalog);
$("filterSearch").addEventListener("input", renderCatalog);
$("clearFiltersBtn").addEventListener("click", ()=>{
  $("filterCategoria").value = "";
  $("filterNivel").value = "";
  $("filterSearch").value = "";
  renderCatalog();
});

function openCourseDetailModal(c){
  state.selectedCourseForModal = c;
  const parsed = parseCourseDescription(c.descripcion);
  $("cdmTitle").textContent = c.nombre;

  $("cdmContent").innerHTML = `
    <div style="display:flex;gap:8px;margin-bottom:16px;flex-wrap:wrap">
      <span class="course-cat">${escapeHtml(c.categoria)}</span>
      <span class="course-level-badge ${getLevelBadgeClass(c.nivel)}">${escapeHtml(c.nivel)}</span>
      <span class="pill">⏱️ ${escapeHtml(c.duracion)} h</span>
    </div>

    ${parsed.prerrequisitos ? `
      <div class="course-detail-section">
        <div class="course-detail-section-title">Prerrequisitos</div>
        <p>${escapeHtml(parsed.prerrequisitos)}</p>
      </div>
    ` : ''}

    ${parsed.temario ? `
      <div class="course-detail-section">
        <div class="course-detail-section-title">Temario</div>
        <p>${escapeHtml(parsed.temario)}</p>
      </div>
    ` : ''}

    ${parsed.herramientas ? `
      <div class="course-detail-section">
        <div class="course-detail-section-title">Herramientas</div>
        <p>${escapeHtml(parsed.herramientas)}</p>
      </div>
    ` : ''}

    <div class="course-detail-section">
      <div class="course-detail-section-title">Al completar</div>
      <p>${escapeHtml(parsed.resumen || c.descripcion)}</p>
    </div>
  `;

  $("courseDetailModalOverlay").classList.add("show");
}

function closeCourseDetailModal(){
  $("courseDetailModalOverlay").classList.remove("show");
  state.selectedCourseForModal = null;
}
$("cdmClose").addEventListener("click", closeCourseDetailModal);
$("cdmCloseBtn").addEventListener("click", closeCourseDetailModal);
$("courseDetailModalOverlay").addEventListener("click", (e)=>{
  if(e.target === $("courseDetailModalOverlay")) closeCourseDetailModal();
});
$("cdmAskAiBtn").addEventListener("click", ()=>{
  if(state.selectedCourseForModal){
    const c = state.selectedCourseForModal;
    closeCourseDetailModal();
    showPanel("consulta");
    $("consultaTexto").value = `¿Qué perfil laboral se obtiene con "${c.nombre}"?`;
    updateCharCounter();
    $("consultaTexto").focus();
  }
});

/* -------------------------------------------------------------
 * Admin: Course Editing
 * ------------------------------------------------------------- */
const modalOverlay = $("modalOverlay");
const courseForm = $("courseForm");
const COURSE_FIELDS = ["cf-nombre", "cf-desc", "cf-cat", "cf-nivel", "cf-dur"];

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

const closeModal = () => modalOverlay.classList.remove("show");
$("newCourseBtn").addEventListener("click", () => openCourseModal(null));
$("modalClose").addEventListener("click", closeModal);
modalOverlay.addEventListener("click", (e)=>{ if(e.target === modalOverlay) closeModal(); });

function closeAllModals(){
  document.querySelectorAll(".modal-overlay").forEach(m => m.classList.remove("show"));
}

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
    estado: $("courseActivo").value === "true"
  };

  let valid = true;
  if(!data.nombre){ setFieldError("cf-nombre"); valid = false; }
  if(!data.descripcion){ setFieldError("cf-desc"); valid = false; }
  if(!data.categoriaId){ setFieldError("cf-cat"); valid = false; }
  if(!data.nivelId){ setFieldError("cf-nivel"); valid = false; }
  if(!(data.duracion > 0)){ setFieldError("cf-dur"); valid = false; }
  if(!valid) return;

  try{
    await api.guardarCurso(id || null, data);
    toast(id ? "Curso actualizado." : "Curso guardado.");
    closeModal();
    refreshCourses();
  }catch(err){
    toast(err.message, true);
  }
});

function renderAdmin(){
  const tbody = $("adminTbody");
  const all = state.todosCursos;
  $("adminCount").textContent = `${all.length} curso(s)`;
  tbody.innerHTML = "";
  all.forEach(c => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>
        <strong>${escapeHtml(c.nombre)}</strong>
        <div style="color:var(--text-faint);font-size:.78rem;max-width:280px;line-height:1.4;margin-top:2px">
          ${escapeHtml(c.descripcion.slice(0, 80))}${c.descripcion.length > 80 ? "…" : ""}
        </div>
      </td>
      <td>${escapeHtml(c.categoria)}</td>
      <td><span class="course-level-badge ${getLevelBadgeClass(c.nivel)}">${escapeHtml(c.nivel)}</span></td>
      <td>${escapeHtml(c.duracion)} h</td>
      <td><span class="state-dot ${c.estado ? 'on' : 'off'}"></span>${c.estado ? 'Activo' : 'Inactivo'}</td>
      <td>
        <div class="admin-actions">
          <button class="btn btn-ghost btn-sm edit-btn">Editar</button>
          ${c.estado ? '<button class="btn btn-danger btn-sm deact-btn">Desactivar</button>' : ''}
        </div>
      </td>`;
    tr.querySelector(".edit-btn").addEventListener("click", () => openCourseModal(c));
    const deact = tr.querySelector(".deact-btn");
    if(deact){
      deact.addEventListener("click", async ()=>{
        if(!confirm(`¿Desactivar "${c.nombre}"?`)) return;
        try{
          await api.desactivarCurso(c.id);
          toast("Curso desactivado.");
          refreshCourses();
        }catch(err){
          toast(err.message, true);
        }
      });
    }
    tbody.appendChild(tr);
  });
}

/* -------------------------------------------------------------
 * Admin: Student Accounts Management
 * ------------------------------------------------------------- */
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
  if(!data.nivelExperiencia){ setFieldError(fields.nivel); ok = false; }
  if(!data.areaInteres){ setFieldError(fields.interes); ok = false; }
  return ok ? data : null;
}

registroForm.addEventListener("submit", async (e)=>{
  e.preventDefault();
  const data = readStudentForm(ADMIN_STUDENT_INPUTS, ADMIN_STUDENT_FIELDS);
  if(!data) return;

  const btn = registroForm.querySelector("button[type=submit]");
  btn.disabled = true;
  btn.textContent = "Guardando…";
  try{
    const student = await api.crearEstudiante(data);
    toast("Estudiante registrado.");
    $("newStudentInfo").style.display = "block";
    $("newStudentIdValue").textContent = maskUuid(student.id);
    $("newStudentIdChip").onclick = () => copyToClipboard(student.id, "ID copiado.");
    registroForm.reset();
    renderStudents();
  }catch(err){
    setFieldError("f-email", err.message);
    toast(err.message, true);
  }finally{
    btn.disabled = false;
    btn.textContent = "Crear estudiante";
  }
});

async function renderStudents(){
  const tbody = $("studentsTbody");
  tbody.innerHTML = `<tr><td colspan="5"><div class="loading-row"><span class="spinner"></span> Cargando…</div></td></tr>`;
  try{
    const students = await api.estudiantes();
    if(students.length === 0){
      tbody.innerHTML = `<tr><td colspan="5" style="color:var(--text-faint);text-align:center;padding:20px">No hay estudiantes.</td></tr>`;
      return;
    }
    tbody.innerHTML = "";
    students.forEach(s => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>
          <span class="masked-uuid-chip copy-student-uuid" title="Copiar ID">${escapeHtml(maskUuid(s.id))} 📋</span>
        </td>
        <td>
          <strong>${escapeHtml(s.nombre)}</strong>
          <div style="color:var(--text-faint);font-size:.78rem">${escapeHtml(s.email)}</div>
        </td>
        <td><span class="pill">${escapeHtml(s.nivelExperiencia)}</span></td>
        <td><span class="pill">${escapeHtml(s.areaInteres || "—")}</span></td>
        <td>${s.id === state.user?.id ? '<span class="pill" style="opacity:.7">Tu cuenta</span>' : '<button class="btn btn-danger btn-sm del-student-btn">Eliminar</button>'}</td>`;

      tr.querySelector(".copy-student-uuid").addEventListener("click", () => copyToClipboard(s.id, "ID copiado."));
      const delBtn = tr.querySelector(".del-student-btn");
      if(delBtn){
        delBtn.addEventListener("click", async ()=>{
          if(!confirm(`¿Eliminar la cuenta de ${s.nombre}?`)) return;
          try{
            await api.eliminarEstudiante(s.id);
            toast("Estudiante eliminado.");
            renderStudents();
          }catch(err){
            toast(err.message, true);
          }
        });
      }
      tbody.appendChild(tr);
    });
  }catch(e){
    tbody.innerHTML = `<tr><td colspan="5" style="color:var(--danger)">Error: ${escapeHtml(e.message)}</td></tr>`;
  }
}

/* -------------------------------------------------------------
 * Consulta Inteligente
 * ------------------------------------------------------------- */
function updateCharCounter(){
  const len = ($("consultaTexto").value || "").length;
  $("charCounter").textContent = `${len} / 600`;
}
$("consultaTexto").addEventListener("input", updateCharCounter);

EXAMPLES.forEach(ex => {
  const chip = document.createElement("span");
  chip.className = "example-chip";
  chip.textContent = ex;
  chip.addEventListener("click", ()=>{
    $("consultaTexto").value = ex;
    updateCharCounter();
    $("consultaTexto").focus();
  });
  $("examples").appendChild(chip);
});

$("consultaSubmit").addEventListener("click", async ()=>{
  const field = $("consultaTexto").closest(".field");
  const texto = $("consultaTexto").value.trim();
  field.classList.remove("has-error");

  if(!texto){
    field.classList.add("has-error");
    $("consultaTexto").focus();
    return;
  }

  const btn = $("consultaSubmit");
  $("resultCard").style.display = "none";
  $("consultaLoading").style.display = "block";
  btn.disabled = true;

  try{
    const result = await api.consultar(texto);
    renderResult(result);
  }catch(err){
    renderResult({
      estado: "Error",
      pregunta: texto,
      recomendacion: null,
      errorMsg: err.message
    });
  }finally{
    $("consultaLoading").style.display = "none";
    btn.disabled = false;
  }
});

function getSimilarityDetails(simVal){
  if(simVal === null || simVal === undefined || isNaN(simVal)){
    return {percent: 0, text: "—", label: "Similitud N/D"};
  }
  const num = Number(simVal);
  const percent = num <= 1 ? (num * 100) : num;
  const formatted = percent.toFixed(1) + "%";

  let label = "Afinidad moderada";
  if(percent >= 90) label = "Afinidad muy alta";
  else if(percent >= 75) label = "Afinidad alta";

  return {percent: Math.min(100, Math.max(0, percent)), text: formatted, label};
}

function sourcesHtml(fuentes, compact = false){
  if(!fuentes || fuentes.length === 0){
    return '<p style="color:var(--text-muted);font-size:.85rem;padding:6px 0">No se recuperaron fuentes.</p>';
  }

  return fuentes.map(f => {
    const nombre = f.cursoNombre || f.nombre || "Curso del Catálogo";
    const categoria = f.categoriaNombre || f.categoria || "General";
    const nivel = f.nivelNombre || f.nivel || "General";
    const duracion = f.duracion ? `${f.duracion} h` : "";
    const sim = getSimilarityDetails(f.similitud);

    return `
      <div class="source-item">
        <div class="source-main">
          <h4>${escapeHtml(nombre)}</h4>
          <div class="source-tags">
            <span class="course-cat">${escapeHtml(categoria)}</span>
            <span class="course-level-badge ${getLevelBadgeClass(nivel)}">${escapeHtml(nivel)}</span>
            ${duracion ? `<span class="pill">⏱️ ${escapeHtml(duracion)}</span>` : ''}
          </div>
        </div>
        <div class="similarity-box">
          <div class="similarity-header">
            <span class="similarity-label">Afinidad:</span>
            <span class="similarity-value">${escapeHtml(sim.text)}</span>
          </div>
          <div class="similarity-meter" title="Similitud: ${escapeHtml(sim.text)}">
            <div class="similarity-meter-fill" style="width: ${sim.percent}%"></div>
          </div>
          <span class="similarity-text-badge">${escapeHtml(sim.label)}</span>
        </div>
      </div>
    `;
  }).join("");
}

function renderResult(c){
  const card = $("resultCard");
  card.style.display = "block";
  const rec = c.recomendacion || (c.respuesta ? {
    id: c.recomendacionId,
    contenido: c.respuesta,
    fuentes: c.fuentes || []
  } : null);

  let body = `
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:14px">
      <span class="result-status ${statusClass(c.estado)}">
        ● ${escapeHtml(c.estado)}
      </span>
    </div>
  `;

  if(c.estado === "Respondida" && rec){
    body += `
      <div class="recommendation-box">
        <div class="recommendation-title">
          <span>✦</span> Recomendación personalizada
        </div>
        <div class="result-answer">
          ${renderSafeMarkdown(rec.contenido || c.respuesta || "")}
        </div>
      </div>
    `;

    const fuentes = (rec.fuentes && rec.fuentes.length > 0) ? rec.fuentes : (c.fuentes || []);
    body += `
      <div class="sources-head">
        <span>📚</span> Cursos recomendados (${fuentes.length})
      </div>
    `;
    body += sourcesHtml(fuentes, false);
    body += ratingBlockHtml();
  } else if(c.estado === "Sin resultados"){
    body += `
      <div style="padding:16px;background:rgba(245,158,11,0.08);border:1px solid rgba(245,158,11,0.25);border-radius:var(--radius-m)">
        <h4 style="color:var(--accent);margin:0 0 6px">Sin coincidencias</h4>
        <p style="color:var(--text-muted);font-size:.9rem;line-height:1.5;margin:0">
          No encontramos cursos con afinidad suficiente en el catálogo. Prueba consultando por desarrollo web, backend, inteligencia artificial, bases de datos o ciberseguridad.
        </p>
      </div>
    `;
  } else {
    body += `
      <div style="padding:14px;background:rgba(239,68,68,0.08);border:1px solid rgba(239,68,68,0.25);border-radius:var(--radius-m)">
        <p style="color:var(--danger);margin:0">
          No se pudo completar la consulta${c.errorMsg ? ": " + escapeHtml(c.errorMsg) : ""}. Intenta nuevamente.
        </p>
      </div>
    `;
  }

  card.innerHTML = body;

  if(c.estado === "Respondida" && rec){
    attachRatingHandlers(rec.id || c.recomendacionId, card);
  }

  card.scrollIntoView({behavior:"smooth", block:"start"});
}

function ratingBlockHtml(){
  return `
    <div class="rating-block">
      <div class="rating-prompt">Califica esta respuesta:</div>
      <div class="stars">
        ${[1,2,3,4,5].map(n => `<button type="button" class="star" data-n="${n}" title="${n} estrellas">${n}</button>`).join("")}
      </div>
      <textarea placeholder="Comentario opcional..." class="rating-comment"></textarea>
      <button class="btn btn-ghost btn-sm rating-submit" style="margin-top:10px">Enviar calificación</button>
    </div>
  `;
}

function attachRatingHandlers(recomendacionId, scope){
  let selected = 0;
  const stars = scope.querySelectorAll(".star");
  stars.forEach(st => {
    st.addEventListener("click", ()=>{
      selected = Number(st.dataset.n);
      stars.forEach(s => s.classList.toggle("on", Number(s.dataset.n) <= selected));
    });
  });

  const submitBtn = scope.querySelector(".rating-submit");
  if(!submitBtn) return;

  submitBtn.addEventListener("click", async (e)=>{
    if(!selected){
      toast("Selecciona una puntuación (1-5).", true);
      return;
    }
    const comentario = scope.querySelector(".rating-comment").value.trim();
    e.target.disabled = true;
    e.target.textContent = "Guardando…";
    try{
      await api.calificar(recomendacionId, selected, comentario);
      toast("Calificación guardada.");
      scope.querySelector(".rating-block").innerHTML = `
        <div style="padding:10px 14px;background:rgba(16,185,129,0.1);border:1px solid rgba(16,185,129,0.3);border-radius:var(--radius-s);color:var(--accent-2);font-size:.88rem">
          ✓ Calificación registrada (${selected}/5): ${"★".repeat(selected)}
        </div>
      `;
      loadStats().catch(()=>{});
    }catch(err){
      toast(err.message, true);
      e.target.disabled = false;
      e.target.textContent = "Enviar calificación";
    }
  });
}

/* -------------------------------------------------------------
 * Historial
 * ------------------------------------------------------------- */
async function renderHistory(){
  const list = $("historyList");
  list.innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando…</div>';
  try{
    const items = await api.historial(state.user.id);
    if(items.length === 0){
      list.innerHTML = `
        <div class="empty-state">
          <h3>Sin consultas</h3>
          <p>Tus recomendaciones anteriores aparecerán aquí.</p>
        </div>
      `;
      return;
    }

    list.innerHTML = "";
    items.forEach(q => {
      const rec = q.recomendacion;
      const answered = q.estado === "Respondida" && (rec || q.resumen);
      const cantFuentes = (rec && rec.fuentes) ? rec.fuentes.length : (q.cursosRecomendados ? q.cursosRecomendados.length : 0);
      const resumen = answered ? `${cantFuentes} curso(s)` : q.estado;

      const el = document.createElement("div");
      el.className = "history-item";
      el.innerHTML = `
        <div class="history-top">
          <span class="history-q">${escapeHtml(q.pregunta)}</span>
          <span class="result-status ${statusClass(q.estado)}" style="margin:0">${escapeHtml(q.estado)}</span>
        </div>
        <div class="history-date">📅 ${new Date(q.fecha).toLocaleDateString()} · ${escapeHtml(resumen)}</div>
        <div class="history-detail"></div>
      `;

      el.addEventListener("click", ()=>{
        const wasOpen = el.classList.contains("open");
        document.querySelectorAll(".history-item.open").forEach(o => o.classList.remove("open"));
        if(wasOpen) return;
        el.classList.add("open");

        const detail = el.querySelector(".history-detail");
        if(answered){
          const contenido = (rec && rec.contenido) ? rec.contenido : q.resumen;
          const fuentes = (rec && rec.fuentes && rec.fuentes.length > 0) ? rec.fuentes : null;
          const cal = rec ? rec.calificacion : null;

          let detailHtml = `
            <div class="recommendation-box" style="margin-bottom:12px">
              <div class="result-answer">${renderSafeMarkdown(contenido || "")}</div>
            </div>
          `;

          if(fuentes && fuentes.length > 0){
            detailHtml += `<div class="sources-head">Cursos recomendados</div>` + sourcesHtml(fuentes, true);
          } else if(q.cursosRecomendados && q.cursosRecomendados.length > 0){
            detailHtml += `
              <div class="sources-head">Cursos recomendados</div>
              <div class="source-tags" style="margin-top:6px">
                ${q.cursosRecomendados.map(c => `<span class="pill">${escapeHtml(c)}</span>`).join(" ")}
              </div>
            `;
          }

          if(cal){
            detailHtml += `
              <div style="margin-top:10px;padding:8px 12px;background:var(--surface-2);border-radius:var(--radius-s);font-size:.82rem;color:var(--text-muted)">
                Calificación: <strong style="color:var(--accent)">${"★".repeat(cal.puntuacion)}</strong> (${cal.puntuacion}/5)
                ${cal.comentario ? ` — "${escapeHtml(cal.comentario)}"` : ''}
              </div>
            `;
          }
          detail.innerHTML = detailHtml;
        } else {
          detail.innerHTML = `<p style="font-size:.85rem;color:var(--text-muted);margin:0">Estado: ${escapeHtml(q.estado)}</p>`;
        }
      });

      list.appendChild(el);
    });
  }catch(e){
    list.innerHTML = `<div class="empty-state"><p style="color:var(--danger)">Error: ${escapeHtml(e.message)}</p></div>`;
  }
}

/* -------------------------------------------------------------
 * Estadísticas
 * ------------------------------------------------------------- */
async function loadStats(){
  const s = await api.estadisticas();
  $("heroCourses").textContent = state.cursos.length;
  $("heroAnswered").textContent = s.respondidas ?? s.consultasRespondidas ?? 0;
  $("heroAvgRating").textContent = fmtRating(s.promedioCalificacion ?? s.promedioCalificaciones);
  return s;
}

async function renderStats(){
  const grid = $("statGrid");
  grid.innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando…</div>';
  try{
    const s = await loadStats();
    grid.innerHTML = `
      <div class="stat-card">
        <b>${s.total ?? s.totalConsultas ?? 0}</b>
        <span>consultas totales</span>
      </div>
      <div class="stat-card">
        <b>${s.respondidas ?? s.consultasRespondidas ?? 0}</b>
        <span>respondidas</span>
      </div>
      <div class="stat-card">
        <b>${s.sinResultados ?? s.consultasSinResultados ?? 0}</b>
        <span>sin resultados</span>
      </div>
      <div class="stat-card">
        <b>${fmtRating(s.promedioCalificacion ?? s.promedioCalificaciones)}</b>
        <span>calificación promedio</span>
      </div>
    `;
    $("topCourse").textContent = s.cursoMasRecomendado || "Aún no hay suficientes datos.";
  }catch(e){
    grid.innerHTML = '<div class="empty-state"><p>Error al cargar estadísticas.</p></div>';
  }
}

/* -------------------------------------------------------------
 * Initialization
 * ------------------------------------------------------------- */
fillCategoriaSelects();

const saved = loadSession();
if(saved && saved.token){
  state.token = saved.token;
  loginScreen.style.display = "none";
  api.me()
    .then(u => {
      state.user = normalizeUser(u);
      saveSession();
      enterApp();
    })
    .catch(()=>{
      if(state.token) logout();
    });
} else {
  showLogin();
  loadPublicCategorias();
}

})();