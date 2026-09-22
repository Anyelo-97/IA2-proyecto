(function(){
"use strict";

function loadLocal(key, fallback){
  try{
    const raw = localStorage.getItem(key);
    return raw ? JSON.parse(raw) : fallback;
  }catch(e){ return fallback; }
}
function saveLocal(key, value){
  try{ localStorage.setItem(key, JSON.stringify(value)); }catch(e){ /* ignore */ }
}

const SEED_COURSES = [
  {id:1,nombre:"Fundamentos de HTML y CSS",descripcion:"Construye páginas web desde cero: estructura semántica con HTML5, estilos con CSS3, diseño responsivo con flexbox y grid.",categoria:"Desarrollo Web",nivel:"Básico",duracion:20,activo:true},
  {id:2,nombre:"JavaScript esencial",descripcion:"Programación en JavaScript moderno: variables, funciones, DOM, eventos, fetch API y manejo asíncrono con async/await.",categoria:"Desarrollo Web",nivel:"Básico",duracion:30,activo:true},
  {id:3,nombre:"Desarrollo Frontend con React",descripcion:"Crea interfaces web interactivas con React: componentes, estado, hooks y consumo de APIs REST.",categoria:"Desarrollo Web",nivel:"Intermedio",duracion:35,activo:true},
  {id:4,nombre:"Java desde cero",descripcion:"Introducción a la programación orientada a objetos con Java: clases, herencia, colecciones y manejo de excepciones.",categoria:"Programación",nivel:"Básico",duracion:40,activo:true},
  {id:5,nombre:"Spring Boot para APIs REST",descripcion:"Construye APIs REST profesionales con Java y Spring Boot: controladores, servicios, repositorios, JPA y buenas prácticas de arquitectura.",categoria:"Programación",nivel:"Intermedio",duracion:45,activo:true},
  {id:6,nombre:"Spring Security y autenticación",descripcion:"Protege aplicaciones web construidas con Spring Boot usando JWT, roles, autenticación y autorización.",categoria:"Programación",nivel:"Avanzado",duracion:30,activo:true},
  {id:7,nombre:"Python para principiantes",descripcion:"Aprende a programar en Python: sintaxis, estructuras de control, funciones y manejo de archivos.",categoria:"Programación",nivel:"Básico",duracion:25,activo:true},
  {id:8,nombre:"Bases de datos relacionales con SQL",descripcion:"Diseño de bases de datos relacionales, modelado entidad-relación, consultas SQL, claves foráneas e índices.",categoria:"Bases de Datos",nivel:"Básico",duracion:25,activo:true},
  {id:9,nombre:"PostgreSQL avanzado",descripcion:"Optimización de consultas, transacciones, procedimientos almacenados e índices avanzados en PostgreSQL.",categoria:"Bases de Datos",nivel:"Avanzado",duracion:30,activo:true},
  {id:10,nombre:"Bases de datos NoSQL",descripcion:"Introducción a bases de datos no relacionales: documentos, clave-valor y bases de datos vectoriales como Qdrant.",categoria:"Bases de Datos",nivel:"Intermedio",duracion:20,activo:true},
  {id:11,nombre:"Fundamentos de Inteligencia Artificial",descripcion:"Conceptos clave de inteligencia artificial: aprendizaje automático, redes neuronales y modelos de lenguaje.",categoria:"Inteligencia Artificial",nivel:"Básico",duracion:25,activo:true},
  {id:12,nombre:"Modelos de lenguaje y RAG",descripcion:"Construye aplicaciones con modelos de lenguaje: embeddings, búsqueda semántica y arquitecturas de generación aumentada por recuperación (RAG).",categoria:"Inteligencia Artificial",nivel:"Avanzado",duracion:35,activo:true},
  {id:13,nombre:"Machine Learning práctico",descripcion:"Entrena y evalúa modelos de aprendizaje automático supervisado y no supervisado con datos reales.",categoria:"Inteligencia Artificial",nivel:"Intermedio",duracion:40,activo:true},
  {id:14,nombre:"Automatización de procesos con n8n",descripcion:"Diseña flujos de automatización empresarial sin código usando n8n: disparadores, integraciones y conexión con APIs externas.",categoria:"Automatización",nivel:"Intermedio",duracion:20,activo:true},
  {id:15,nombre:"Integración de APIs y automatización avanzada",descripcion:"Conecta sistemas y automatiza procesos empresariales complejos integrando APIs REST, webhooks y flujos condicionales.",categoria:"Automatización",nivel:"Avanzado",duracion:25,activo:true},
  {id:16,nombre:"Análisis de datos con Python",descripcion:"Limpieza, transformación y análisis exploratorio de datos usando pandas y numpy.",categoria:"Análisis de Datos",nivel:"Intermedio",duracion:30,activo:true},
  {id:17,nombre:"Construcción de paneles de control",descripcion:"Diseña dashboards y paneles de control interactivos para visualizar datos y apoyar la toma de decisiones.",categoria:"Análisis de Datos",nivel:"Intermedio",duracion:25,activo:true},
  {id:18,nombre:"Estadística aplicada para análisis de datos",descripcion:"Fundamentos estadísticos para interpretar datos: distribuciones, correlación y pruebas de hipótesis.",categoria:"Análisis de Datos",nivel:"Básico",duracion:20,activo:true},
  {id:19,nombre:"Docker y contenedores",descripcion:"Empaqueta y despliega aplicaciones usando contenedores Docker, imágenes, volúmenes y Docker Compose.",categoria:"Herramientas Tecnológicas",nivel:"Intermedio",duracion:20,activo:true},
  {id:20,nombre:"Control de versiones con Git y GitHub",descripcion:"Flujo de trabajo colaborativo con Git y GitHub: ramas, pull requests y resolución de conflictos.",categoria:"Herramientas Tecnológicas",nivel:"Básico",duracion:12,activo:true},
  {id:21,nombre:"Swagger y documentación de APIs",descripcion:"Documenta y prueba APIs REST de forma interactiva usando Swagger y OpenAPI.",categoria:"Herramientas Tecnológicas",nivel:"Básico",duracion:10,activo:true},
  {id:22,nombre:"Despliegue de aplicaciones en la nube",descripcion:"Publica aplicaciones web y APIs en proveedores cloud, variables de entorno y buenas prácticas de despliegue.",categoria:"Herramientas Tecnológicas",nivel:"Avanzado",duracion:22,activo:true}
];

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

const STOPWORDS = new Set(["de","la","el","en","y","a","que","los","las","un","una","para","con","por","como","quiero","necesito","me","interesa","aprender","puedo","estudiar","trabajar","qué","¿","?","."]);

function tokenize(s){
  return s.toLowerCase()
    .replace(/[¿?¡!.,;:]/g," ")
    .split(/\s+/)
    .filter(w=>w && !STOPWORDS.has(w));
}
function mockSimilarity(query, course){
  const qTokens = new Set(tokenize(query));
  const cTokens = tokenize(course.nombre + " " + course.descripcion + " " + course.categoria);
  if(qTokens.size===0 || cTokens.length===0) return 0;
  let hits = 0;
  cTokens.forEach(t=>{ if(qTokens.has(t)) hits++; });
  const base = hits / Math.max(3, qTokens.size);
  return Math.max(0, Math.min(0.97, base*0.9 + (hits>0?0.25:0)));
}
const RELEVANCE_THRESHOLD = 0.28;

const DEMO_ADMIN = {email:"admin@rutaia.edu", password:"admin123", nombre:"Administrador RutaIA"};

const state = {
  apiBase: loadLocal("rutaia_apibase", ""),
  demo: true,
  students: loadLocal("rutaia_students", []),
  courses: loadLocal("rutaia_courses", null) || JSON.parse(JSON.stringify(SEED_COURSES)),
  queries: loadLocal("rutaia_queries", []),
  currentUser: loadLocal("rutaia_session", null), // {role, id, nombre, email}
  token: loadLocal("rutaia_token", null),
  nextIds: loadLocal("rutaia_nextids", {student:1, course:23, query:1})
};
function persist(){
  saveLocal("rutaia_students", state.students);
  saveLocal("rutaia_courses", state.courses);
  saveLocal("rutaia_queries", state.queries);
  saveLocal("rutaia_session", state.currentUser);
  saveLocal("rutaia_token", state.token);
  saveLocal("rutaia_nextids", state.nextIds);
}

let toastTimer=null;
function toast(msg, isErr){
  const el = document.getElementById("toast");
  el.textContent = msg;
  el.className = "toast show" + (isErr? " err":"");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(()=>{ el.classList.remove("show"); }, 3200);
}


const mockApi = {
  delay(ms){ return new Promise(r=>setTimeout(r, ms||350)); },

  async loginEstudiante(identifier, password){
    await this.delay(300);
    const idNum = Number(identifier);
    const s = state.students.find(s=>
      s.email.toLowerCase()===identifier.toLowerCase() || (!isNaN(idNum) && s.id===idNum)
    );
    if(!s || s.password !== password) throw new Error("Correo/ID o contraseña incorrectos.");
    return {role:"estudiante", id:s.id, nombre:s.nombre, email:s.email};
  },
  async loginAdmin(email, password){
    await this.delay(300);
    if(email.toLowerCase()!==DEMO_ADMIN.email.toLowerCase() || password!==DEMO_ADMIN.password){
      throw new Error("Correo o contraseña incorrectos.");
    }
    return {role:"admin", id:0, nombre:DEMO_ADMIN.nombre, email:DEMO_ADMIN.email};
  },

  async crearEstudiante(data){
    await this.delay();
    const exists = state.students.some(s=>s.email.toLowerCase()===data.email.toLowerCase());
    if(exists) throw new Error("Ese correo ya está registrado.");
    const student = {id: state.nextIds.student++, ...data};
    state.students.push(student);
    persist();
    return student;
  },
  async listarEstudiantes(){ await this.delay(150); return state.students; },
  async eliminarEstudiante(id){
    await this.delay();
    const idx = state.students.findIndex(s=>s.id===id);
    if(idx===-1) throw new Error("Estudiante no encontrado.");
    state.students.splice(idx,1);
    state.queries = state.queries.filter(q=>q.estudianteId!==id);
    persist();
    return true;
  },

  async listarCursos({categoria, nivel}={}){
    await this.delay(200);
    return state.courses.filter(c=>{
      if(!c.activo) return false;
      if(categoria && c.categoria!==categoria) return false;
      if(nivel && c.nivel!==nivel) return false;
      return true;
    });
  },
  async crearCurso(data){
    await this.delay();
    if(!data.nombre || !data.descripcion) throw new Error("Nombre y descripción son obligatorios.");
    if(!(data.duracion>0)) throw new Error("La duración debe ser mayor que cero.");
    const course = {id: state.nextIds.course++, activo:true, ...data};
    state.courses.push(course);
    persist();
    return course;
  },
  async actualizarCurso(id, data){
    await this.delay();
    const c = state.courses.find(c=>c.id===id);
    if(!c) throw new Error("Curso no encontrado.");
    Object.assign(c, data);
    persist();
    return c;
  },
  async desactivarCurso(id){
    await this.delay();
    const c = state.courses.find(c=>c.id===id);
    if(!c) throw new Error("Curso no encontrado.");
    c.activo = false;
    persist();
    return c;
  },

  async consultar({estudianteId, pregunta}){
    await this.delay(700);
    const student = state.students.find(s=>s.id===estudianteId);
    if(!student) throw new Error("El estudiante no existe.");
    if(!pregunta || !pregunta.trim()) throw new Error("La pregunta no puede estar vacía.");

    const registro = {
      id: state.nextIds.query++,
      estudianteId, pregunta, fecha: new Date().toISOString(),
      estado:"Pendiente", respuesta:null, fuentes:[]
    };
    state.queries.unshift(registro);
    persist();

    const activos = state.courses.filter(c=>c.activo);
    const scored = activos.map(c=>({course:c, sim: mockSimilarity(pregunta, c)}))
      .sort((a,b)=>b.sim-a.sim);
    const relevant = scored.filter(s=>s.sim>=RELEVANCE_THRESHOLD).slice(0,5);

    if(relevant.length===0){
      registro.estado = "Sin resultados";
      persist();
      return registro;
    }

    const fuentes = relevant.map(r=>({
      cursoId:r.course.id, nombre:r.course.nombre, descripcion:r.course.descripcion,
      categoria:r.course.categoria, nivel:r.course.nivel, similitud: Number(r.sim.toFixed(2))
    }));

    const top = fuentes[0];
    const respuesta = `Según el catálogo, "${top.nombre}" (${top.categoria}, nivel ${top.nivel}) es la recomendación más cercana a tu consulta.` +
      (fuentes.length>1 ? ` También podrían interesarte: ${fuentes.slice(1).map(f=>f.nombre).join(", ")}.` : "") +
      ` La recomendación se basa únicamente en los cursos recuperados del catálogo; no se sugieren cursos fuera de esta lista.`;

    registro.estado = "Respondida";
    registro.respuesta = respuesta;
    registro.fuentes = fuentes;
    registro.calificacion = null;
    persist();
    return registro;
  },

  async historial(estudianteId){
    await this.delay(200);
    return state.queries.filter(q=>q.estudianteId===estudianteId);
  },

  async calificar(consultaId, puntuacion, comentario){
    await this.delay(200);
    const q = state.queries.find(q=>q.id===consultaId);
    if(!q) throw new Error("Consulta no encontrada.");
    if(q.calificacion) throw new Error("Esta recomendación ya fue calificada.");
    q.calificacion = {puntuacion, comentario: comentario||""};
    persist();
    return q.calificacion;
  },

  async estadisticas(){
    await this.delay(150);
    const total = state.queries.length;
    const respondidas = state.queries.filter(q=>q.estado==="Respondida").length;
    const sinResultados = state.queries.filter(q=>q.estado==="Sin resultados").length;
    const errores = state.queries.filter(q=>q.estado==="Error").length;
    const ratings = state.queries.filter(q=>q.calificacion).map(q=>q.calificacion.puntuacion);
    const avg = ratings.length ? (ratings.reduce((a,b)=>a+b,0)/ratings.length) : null;
    const counts = {};
    state.queries.forEach(q=> (q.fuentes||[]).forEach(f=>{ counts[f.nombre]=(counts[f.nombre]||0)+1; }));
    let top = null, topN = 0;
    Object.entries(counts).forEach(([n,c])=>{ if(c>topN){topN=c; top=n;} });
    return {total, respondidas, sinResultados, errores, promedioCalificacion: avg, cursoMasRecomendado: top};
  }
};

/* ============ REAL API ============ */
async function realFetch(path, opts){
  const headers = {"Content-Type":"application/json"};
  if(state.token) headers["Authorization"] = "Bearer " + state.token;
  const res = await fetch(state.apiBase.replace(/\/$/,"") + path, {headers, ...opts});
  if(!res.ok){
    let msg = "Error del servidor ("+res.status+")";
    try{ const body = await res.json(); if(body.message) msg = body.message; }catch(e){}
    throw new Error(msg);
  }
  if(res.status===204) return null;
  return res.json();
}
const realApi = {
  loginEstudiante:(email,password)=> realFetch("/auth/login", {method:"POST", body:JSON.stringify({email,password,role:"estudiante"})}),
  loginAdmin:(email,password)=> realFetch("/auth/login", {method:"POST", body:JSON.stringify({email,password,role:"admin"})}),
  crearEstudiante:(data)=> realFetch("/estudiantes", {method:"POST", body:JSON.stringify(data)}),
  listarEstudiantes:()=> realFetch("/estudiantes"),
  eliminarEstudiante:(id)=> realFetch("/estudiantes/"+id, {method:"DELETE"}),
  listarCursos:({categoria,nivel}={})=>{
    const qs = new URLSearchParams();
    if(categoria) qs.set("categoria",categoria);
    if(nivel) qs.set("nivel",nivel);
    const suffix = qs.toString() ? "?"+qs.toString() : "";
    return realFetch("/cursos"+suffix);
  },
  crearCurso:(data)=> realFetch("/cursos", {method:"POST", body:JSON.stringify(data)}),
  actualizarCurso:(id,data)=> realFetch("/cursos/"+id, {method:"PUT", body:JSON.stringify(data)}),
  desactivarCurso:(id)=> realFetch("/cursos/"+id+"/desactivar", {method:"PATCH"}),
  consultar:(data)=> realFetch("/consultas", {method:"POST", body:JSON.stringify(data)}),
  historial:(estudianteId)=> realFetch("/estudiantes/"+estudianteId+"/historial"),
  calificar:(consultaId, puntuacion, comentario)=> realFetch("/consultas/"+consultaId+"/calificacion", {method:"POST", body:JSON.stringify({puntuacion, comentario})}),
  estadisticas:()=> realFetch("/estadisticas")
};
function api(){ return state.demo ? mockApi : realApi; }

/* ============ LOGIN SCREEN ============ */
const loginScreen = document.getElementById("loginScreen");
const appRoot = document.getElementById("appRoot");

document.querySelectorAll(".role-toggle button").forEach(btn=>{
  btn.addEventListener("click", ()=>{
    document.querySelectorAll(".role-toggle button").forEach(b=>b.classList.remove("active"));
    btn.classList.add("active");
    const role = btn.dataset.role;
    document.getElementById("loginFormEstudiante").classList.toggle("active", role==="estudiante");
    document.getElementById("loginFormAdmin").classList.toggle("active", role==="admin");
  });
});
document.getElementById("adminDemoHint").style.display = state.demo ? "block" : "none";

document.getElementById("loginFormEstudiante").addEventListener("submit", async (e)=>{
  e.preventDefault();
  document.getElementById("lf-est-email").classList.remove("has-error");
  const email = document.getElementById("loginEstEmail").value.trim();
  const password = document.getElementById("loginEstPass").value;
  if(!email || !password){ document.getElementById("lf-est-email").classList.add("has-error"); return; }
  const btn = e.target.querySelector("button[type=submit]");
  btn.disabled = true; btn.textContent = "Ingresando…";
  try{
    const res = await api().loginEstudiante(email, password);
    completeLogin(res, res.token);
  }catch(err){
    document.getElementById("lf-est-email").classList.add("has-error");
    document.querySelector("#lf-est-email .error-text").textContent = err.message;
  }finally{
    btn.disabled = false; btn.textContent = "Ingresar";
  }
});

document.getElementById("loginFormAdmin").addEventListener("submit", async (e)=>{
  e.preventDefault();
  document.getElementById("lf-adm-email").classList.remove("has-error");
  const email = document.getElementById("loginAdmEmail").value.trim();
  const password = document.getElementById("loginAdmPass").value;
  if(!email || !password){ document.getElementById("lf-adm-email").classList.add("has-error"); return; }
  const btn = e.target.querySelector("button[type=submit]");
  btn.disabled = true; btn.textContent = "Ingresando…";
  try{
    const res = await api().loginAdmin(email, password);
    completeLogin(res, res.token);
  }catch(err){
    document.getElementById("lf-adm-email").classList.add("has-error");
    document.querySelector("#lf-adm-email .error-text").textContent = err.message;
  }finally{
    btn.disabled = false; btn.textContent = "Ingresar";
  }
});

function completeLogin(user, token){
  state.currentUser = {role:user.role, id:user.id, nombre:user.nombre, email:user.email};
  state.token = token || null;
  persist();
  enterApp();
}

document.getElementById("logoutBtn").addEventListener("click", ()=>{
  state.currentUser = null;
  state.token = null;
  persist();
  loginScreen.style.display = "flex";
  appRoot.style.display = "none";
  document.getElementById("loginFormEstudiante").reset();
  document.getElementById("loginFormAdmin").reset();
});


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
  const role = state.currentUser.role;
  const nav = document.getElementById("routeNav");
  nav.innerHTML = "";
  NAV[role].forEach(item=>{
    const btn = document.createElement("button");
    btn.className = "route-step";
    btn.dataset.panel = item.panel;
    btn.textContent = item.label;
    btn.addEventListener("click", ()=> showPanel(item.panel));
    nav.appendChild(btn);
  });
  document.getElementById("connBarAdmin").style.display = role==="admin" ? "flex" : "none";
}

function showPanel(name){
  ALL_PANELS.forEach(p=>{
    document.getElementById("panel-"+p).classList.toggle("active", p===name);
  });
  document.querySelectorAll(".route-step").forEach(btn=>{
    btn.classList.toggle("active", btn.dataset.panel===name);
  });
  document.getElementById("hero").style.display = name==="catalogo" ? "" : "none";
  window.scrollTo({top:0, behavior:"smooth"});
  if(name==="historial") renderHistory();
  if(name==="estudiantes") renderStudents();
  if(name==="admin") renderAdmin();
  if(name==="stats") renderStats();
  if(name==="consulta") updateConsultaWho();
}

function enterApp(){
  loginScreen.style.display = "none";
  appRoot.style.display = "block";
  const u = state.currentUser;
  document.getElementById("whoLabel").textContent = u.role==="estudiante" ? u.nombre+" · ID "+u.id : u.nombre;
  document.getElementById("roleBadge").textContent = u.role==="admin" ? "Admin" : "Estudiante";
  document.getElementById("roleBadge").className = "role-badge " + u.role;
  buildNav();
  setConnUI();
  showPanel("catalogo");
  refreshAll();
}


const connDot = document.getElementById("connDot");
const connLabel = document.getElementById("connLabel");
const apiBaseInput = document.getElementById("apiBaseInput");
apiBaseInput.value = state.apiBase;

function setConnUI(){
  if(state.demo){
    connDot.className = "conn-dot off";
    connLabel.textContent = "Modo demostración — datos simulados en el navegador";
  }else{
    connDot.className = "conn-dot";
    connLabel.textContent = "Conectado a " + state.apiBase;
  }
}
document.getElementById("connectBtn").addEventListener("click", ()=>{
  const val = apiBaseInput.value.trim();
  if(!val){ toast("Escribe la URL del API primero.", true); return; }
  state.apiBase = val;
  state.demo = false;
  saveLocal("rutaia_apibase", val);
  setConnUI();
  toast("Conectado. El frontend ahora llama a tu API de Spring Boot.");
  refreshAll();
});
document.getElementById("demoBtn").addEventListener("click", ()=>{
  state.demo = true;
  setConnUI();
  toast("Modo demostración activado.");
  refreshAll();
});


async function renderCatalog(){
  const grid = document.getElementById("courseGrid");
  grid.innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando catálogo…</div>';
  try{
    const categoria = document.getElementById("filterCategoria").value;
    const nivel = document.getElementById("filterNivel").value;
    const courses = await api().listarCursos({categoria, nivel});
    if(courses.length===0){
      grid.innerHTML = '<div class="empty-state">No hay cursos activos que coincidan con estos filtros.</div>';
      return;
    }
    grid.innerHTML = "";
    courses.forEach(c=>{
      const el = document.createElement("div");
      el.className = "course-card";
      el.innerHTML = `
        <span class="course-cat">${escapeHtml(c.categoria)}</span>
        <h3>${escapeHtml(c.nombre)}</h3>
        <p>${escapeHtml(c.descripcion)}</p>
        <div class="course-meta">
          <span class="pill">${escapeHtml(c.nivel)}</span>
          <span class="pill">${c.duracion} h</span>
        </div>`;
      grid.appendChild(el);
    });
    document.getElementById("heroCourses").textContent = (await api().listarCursos()).length;
  }catch(e){
    grid.innerHTML = '<div class="empty-state">No se pudo cargar el catálogo. '+escapeHtml(e.message)+'</div>';
  }
}
async function populateCategoryFilter(){
  const sel = document.getElementById("filterCategoria");
  const interesSel = document.getElementById("regInteres");
  try{
    const all = await api().listarCursos();
    const cats = [...new Set(all.map(c=>c.categoria))].sort();
    sel.innerHTML = '<option value="">Todas las categorías</option>' + cats.map(c=>`<option>${escapeHtml(c)}</option>`).join("");
    if(interesSel) interesSel.innerHTML = '<option value="">Selecciona…</option>' + cats.map(c=>`<option>${escapeHtml(c)}</option>`).join("");
  }catch(e){ /* ignore */ }
}
document.getElementById("filterCategoria").addEventListener("change", renderCatalog);
document.getElementById("filterNivel").addEventListener("change", renderCatalog);


const registroForm = document.getElementById("registroForm");
function clearFieldError(id){ document.getElementById(id).classList.remove("has-error"); }
function setFieldError(id){ document.getElementById(id).classList.add("has-error"); }

registroForm.addEventListener("submit", async (e)=>{
  e.preventDefault();
  ["f-nombre","f-email","f-pass","f-nivel","f-interes"].forEach(clearFieldError);

  const nombre = document.getElementById("regNombre").value.trim();
  const email = document.getElementById("regEmail").value.trim();
  const password = document.getElementById("regPass").value;
  const nivel = document.getElementById("regNivel").value;
  const interes = document.getElementById("regInteres").value;

  let valid = true;
  if(!nombre){ setFieldError("f-nombre"); valid=false; }
  if(!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)){ setFieldError("f-email"); valid=false; }
  if(!password || password.length<6){ setFieldError("f-pass"); valid=false; }
  if(!nivel){ setFieldError("f-nivel"); valid=false; }
  if(!interes){ setFieldError("f-interes"); valid=false; }
  if(!valid) return;

  const btn = registroForm.querySelector("button[type=submit]");
  btn.disabled = true; btn.textContent = "Creando…";
  try{
    const student = await api().crearEstudiante({nombre, email, password, nivel, areaInteres:interes});
    toast("Cuenta creada para "+student.nombre+" (ID: "+student.id+"). Ya puede iniciar sesión.");
    document.getElementById("newStudentInfo").style.display = "block";
    document.getElementById("newStudentIdValue").textContent = student.id;
    registroForm.reset();
    renderStudents();
  }catch(err){
    setFieldError("f-email");
    document.querySelector("#f-email .error-text").textContent = err.message;
    toast(err.message, true);
  }finally{
    btn.disabled = false; btn.textContent = "Crear cuenta de estudiante";
  }
});

async function renderStudents(){
  const tbody = document.getElementById("studentsTbody");
  tbody.innerHTML = `<tr><td colspan="5"><div class="loading-row"><span class="spinner"></span> Cargando…</div></td></tr>`;
  try{
    const students = await api().listarEstudiantes();
    if(students.length===0){
      tbody.innerHTML = `<tr><td colspan="5" style="color:var(--text-faint)">Aún no hay estudiantes registrados.</td></tr>`;
      return;
    }
    tbody.innerHTML = "";
    students.forEach(s=>{
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td><span class="pill" style="font-family:'Space Grotesk';font-weight:600">${s.id}</span></td>
        <td><strong>${escapeHtml(s.nombre)}</strong><div style="color:var(--text-faint);font-size:.78rem">${escapeHtml(s.email)}</div></td>
        <td>${escapeHtml(s.nivel)}</td>
        <td>${escapeHtml(s.areaInteres||"—")}</td>
        <td><button class="btn btn-danger btn-sm del-student-btn">Eliminar</button></td>`;
      tr.querySelector(".del-student-btn").addEventListener("click", async ()=>{
        if(!confirm(`¿Eliminar la cuenta de ${s.nombre} (ID: ${s.id})? Esta acción no se puede deshacer y también borrará su historial de consultas.`)) return;
        try{
          await api().eliminarEstudiante(s.id);
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


function updateConsultaWho(){
  const el = document.getElementById("consultaWho");
  if(el && state.currentUser) el.textContent = state.currentUser.nombre + " (ID " + state.currentUser.id + ")";
}
const examplesEl = document.getElementById("examples");
EXAMPLES.forEach(ex=>{
  const chip = document.createElement("span");
  chip.className = "example-chip";
  chip.textContent = ex;
  chip.addEventListener("click", ()=>{ document.getElementById("consultaTexto").value = ex; });
  examplesEl.appendChild(chip);
});

document.getElementById("consultaSubmit").addEventListener("click", async ()=>{
  const texto = document.getElementById("consultaTexto").value.trim();
  document.getElementById("consultaTexto").closest(".field").classList.remove("has-error");
  if(!texto){
    document.getElementById("consultaTexto").closest(".field").classList.add("has-error");
    return;
  }
  const resultCard = document.getElementById("resultCard");
  const loading = document.getElementById("consultaLoading");
  resultCard.style.display = "none";
  loading.style.display = "flex";
  const btn = document.getElementById("consultaSubmit");
  btn.disabled = true;

  try{
    const result = await api().consultar({estudianteId: state.currentUser.id, pregunta: texto});
    renderResult(result);
  }catch(err){
    renderResult({estado:"Error", pregunta:texto, respuesta:null, fuentes:[], errorMsg: err.message});
  }finally{
    loading.style.display = "none";
    btn.disabled = false;
  }
});

function renderResult(r){
  const card = document.getElementById("resultCard");
  card.style.display = "block";
  const statusKey = r.estado.replace(/\s/g,"");
  const statusClass = "status-" + statusKey.charAt(0).toUpperCase() + statusKey.slice(1).toLowerCase();
  let body = `<span class="result-status ${statusClass}">${escapeHtml(r.estado)}</span>`;

  if(r.estado==="Respondida"){
    body += `<div class="result-answer">${escapeHtml(r.respuesta)}</div>`;
    body += `<div class="sources-head">Fuentes utilizadas (${r.fuentes.length})</div>`;
    r.fuentes.forEach(f=>{
      body += `<div class="source-item">
        <div><h4>${escapeHtml(f.nombre)}</h4><p>${escapeHtml(f.categoria)} · nivel ${escapeHtml(f.nivel)}</p></div>
        <div class="similarity">${Math.round(f.similitud*100)}%</div>
      </div>`;
    });
    body += renderRatingBlock(r.id);
  }else if(r.estado==="Sin resultados"){
    body += `<p style="color:var(--text-muted)">Ningún curso del catálogo superó el umbral de relevancia (${Math.round(RELEVANCE_THRESHOLD*100)}%) para esta pregunta. No se generó una respuesta para evitar recomendar cursos que no existen.</p>`;
  }else if(r.estado==="Error"){
    body += `<p style="color:var(--text-muted)">Ocurrió un error al procesar la consulta${r.errorMsg? ": "+escapeHtml(r.errorMsg):""}. Intenta nuevamente.</p>`;
  }
  card.innerHTML = body;
  if(r.estado==="Respondida") attachRatingHandlers(r.id, card);
}

function renderRatingBlock(queryId){
  return `<div class="rating-block" data-query="${queryId}">
    <label>Califica esta recomendación</label>
    <div class="stars">${[1,2,3,4,5].map(n=>`<button type="button" class="star" data-n="${n}">${n}</button>`).join("")}</div>
    <textarea placeholder="Comentario opcional" class="rating-comment" style="min-height:60px"></textarea>
    <button class="btn btn-ghost btn-sm rating-submit" style="margin-top:10px">Enviar calificación</button>
  </div>`;
}
function attachRatingHandlers(queryId, scope){
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
      await api().calificar(queryId, selected, comentario);
      toast("Gracias por tu calificación.");
      scope.querySelector(".rating-block").innerHTML = "<p style='color:var(--text-muted);font-size:.85rem'>Calificación registrada. ¡Gracias!</p>";
    }catch(err){
      toast(err.message, true);
      e.target.disabled = false;
    }
  });
}


async function renderHistory(){
  const list = document.getElementById("historyList");
  list.innerHTML = '<div class="loading-row"><span class="spinner"></span> Cargando historial…</div>';
  try{
    const items = await api().historial(state.currentUser.id);
    if(items.length===0){ list.innerHTML = '<div class="empty-state">Aún no has hecho consultas.</div>'; return; }
    list.innerHTML = "";
    items.forEach(q=>{
      const el = document.createElement("div");
      el.className = "history-item";
      const dateStr = new Date(q.fecha).toLocaleString();
      const resumen = q.estado==="Respondida" ? (q.fuentes.length + " curso(s) recomendado(s)") : q.estado;
      const statusKey = q.estado.replace(/\s/g,"");
      const statusClass = "status-" + statusKey.charAt(0).toUpperCase() + statusKey.slice(1).toLowerCase();
      el.innerHTML = `
        <div class="history-top">
          <span class="history-q">${escapeHtml(q.pregunta)}</span>
          <span class="result-status ${statusClass}" style="margin:0">${escapeHtml(q.estado)}</span>
        </div>
        <div class="history-date">${dateStr} · ${escapeHtml(resumen)}</div>
        <div class="history-detail"></div>`;
      el.addEventListener("click", ()=>{
        const wasOpen = el.classList.contains("open");
        document.querySelectorAll(".history-item.open").forEach(o=>o.classList.remove("open"));
        if(!wasOpen){
          el.classList.add("open");
          const detail = el.querySelector(".history-detail");
          if(q.estado==="Respondida"){
            detail.innerHTML = `<div class="result-answer" style="font-size:.9rem">${escapeHtml(q.respuesta)}</div>
              <div class="sources-head">Fuentes</div>` +
              q.fuentes.map(f=>`<div class="source-item"><div><h4 style="font-size:.88rem">${escapeHtml(f.nombre)}</h4><p>${escapeHtml(f.categoria)} · ${escapeHtml(f.nivel)}</p></div><div class="similarity">${Math.round(f.similitud*100)}%</div></div>`).join("") +
              (q.calificacion ? `<p style="margin-top:12px;font-size:.85rem;color:var(--text-muted)">Tu calificación: ${"★".repeat(q.calificacion.puntuacion)}${q.calificacion.comentario? " — "+escapeHtml(q.calificacion.comentario):""}</p>` : "");
          }else{
            detail.innerHTML = `<p style="font-size:.85rem;color:var(--text-muted)">Estado: ${escapeHtml(q.estado)}</p>`;
          }
        }
      });
      list.appendChild(el);
    });
  }catch(e){
    list.innerHTML = '<div class="empty-state">No se pudo cargar el historial. '+escapeHtml(e.message)+'</div>';
  }
}


const modalOverlay = document.getElementById("modalOverlay");
const courseForm = document.getElementById("courseForm");
function openCourseModal(course){
  document.getElementById("modalTitle").textContent = course ? "Editar curso" : "Nuevo curso";
  document.getElementById("courseId").value = course ? course.id : "";
  document.getElementById("courseNombre").value = course ? course.nombre : "";
  document.getElementById("courseDesc").value = course ? course.descripcion : "";
  document.getElementById("courseCat").value = course ? course.categoria : "";
  document.getElementById("courseNivel").value = course ? course.nivel : "Básico";
  document.getElementById("courseDur").value = course ? course.duracion : "";
  document.getElementById("courseActivo").value = course ? String(course.activo) : "true";
  ["cf-nombre","cf-desc","cf-cat","cf-nivel","cf-dur"].forEach(clearFieldError);
  modalOverlay.classList.add("show");
}
document.getElementById("newCourseBtn").addEventListener("click", ()=> openCourseModal(null));
document.getElementById("modalClose").addEventListener("click", ()=> modalOverlay.classList.remove("show"));
modalOverlay.addEventListener("click",(e)=>{ if(e.target===modalOverlay) modalOverlay.classList.remove("show"); });

courseForm.addEventListener("submit", async (e)=>{
  e.preventDefault();
  ["cf-nombre","cf-desc","cf-cat","cf-nivel","cf-dur"].forEach(clearFieldError);
  const nombre = document.getElementById("courseNombre").value.trim();
  const descripcion = document.getElementById("courseDesc").value.trim();
  const categoria = document.getElementById("courseCat").value.trim();
  const nivel = document.getElementById("courseNivel").value;
  const duracion = Number(document.getElementById("courseDur").value);
  const activo = document.getElementById("courseActivo").value==="true";
  const id = document.getElementById("courseId").value;

  let valid = true;
  if(!nombre){ setFieldError("cf-nombre"); valid=false; }
  if(!descripcion){ setFieldError("cf-desc"); valid=false; }
  if(!categoria){ setFieldError("cf-cat"); valid=false; }
  if(!(duracion>0)){ setFieldError("cf-dur"); valid=false; }
  if(!valid) return;

  try{
    if(id){
      await api().actualizarCurso(Number(id), {nombre,descripcion,categoria,nivel,duracion,activo});
      toast("Curso actualizado.");
    }else{
      await api().crearCurso({nombre,descripcion,categoria,nivel,duracion});
      toast("Curso creado.");
    }
    modalOverlay.classList.remove("show");
    renderAdmin();
    populateCategoryFilter();
    renderCatalog();
  }catch(err){
    toast(err.message, true);
  }
});

async function renderAdmin(){
  const tbody = document.getElementById("adminTbody");
  tbody.innerHTML = `<tr><td colspan="6"><div class="loading-row"><span class="spinner"></span> Cargando cursos…</div></td></tr>`;
  try{
    const all = state.demo ? state.courses : await realApi.listarCursos();
    document.getElementById("adminCount").textContent = all.length + " curso(s) en el catálogo";
    tbody.innerHTML = "";
    all.forEach(c=>{
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td><strong>${escapeHtml(c.nombre)}</strong><div style="color:var(--text-faint);font-size:.78rem;max-width:280px">${escapeHtml(c.descripcion.slice(0,90))}${c.descripcion.length>90?"…":""}</div></td>
        <td>${escapeHtml(c.categoria)}</td>
        <td>${escapeHtml(c.nivel)}</td>
        <td>${c.duracion} h</td>
        <td><span class="state-dot ${c.activo?'on':'off'}"></span>${c.activo?'Activo':'Inactivo'}</td>
        <td><div class="admin-actions">
          <button class="btn btn-ghost btn-sm edit-btn">Editar</button>
          ${c.activo?'<button class="btn btn-danger btn-sm deact-btn">Desactivar</button>':''}
        </div></td>`;
      tr.querySelector(".edit-btn").addEventListener("click", ()=> openCourseModal(c));
      const deactBtn = tr.querySelector(".deact-btn");
      if(deactBtn) deactBtn.addEventListener("click", async ()=>{
        try{ await api().desactivarCurso(c.id); toast("Curso desactivado."); renderAdmin(); renderCatalog(); }
        catch(err){ toast(err.message, true); }
      });
      tbody.appendChild(tr);
    });
  }catch(e){
    tbody.innerHTML = `<tr><td colspan="6">No se pudieron cargar los cursos. ${escapeHtml(e.message)}</td></tr>`;
  }
}


async function renderStats(){
  const grid = document.getElementById("statGrid");
  grid.innerHTML = '<div class="loading-row"><span class="spinner"></span> Calculando…</div>';
  try{
    const s = await api().estadisticas();
    grid.innerHTML = `
      <div class="stat-card"><b>${s.total}</b><span>consultas totales</span></div>
      <div class="stat-card"><b>${s.respondidas}</b><span>respondidas</span></div>
      <div class="stat-card"><b>${s.sinResultados}</b><span>sin resultados</span></div>
      <div class="stat-card"><b>${s.promedioCalificacion!==null? s.promedioCalificacion.toFixed(1) : "—"}</b><span>calificación promedio</span></div>`;
    document.getElementById("topCourse").textContent = s.cursoMasRecomendado || "Aún no hay suficientes datos.";
    document.getElementById("heroAnswered").textContent = s.respondidas;
    document.getElementById("heroAvgRating").textContent = s.promedioCalificacion!==null ? s.promedioCalificacion.toFixed(1) : "—";
  }catch(e){
    grid.innerHTML = '<div class="empty-state">No se pudieron cargar las estadísticas.</div>';
  }
}


function escapeHtml(str){
  return String(str).replace(/[&<>"']/g, m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[m]));
}

async function refreshAll(){
  await populateCategoryFilter();
  await renderCatalog();
  const role = state.currentUser.role;
  if(role==="admin"){
    renderStudents();
    renderAdmin();
    renderStats();
  }else{
    updateConsultaWho();
    renderHistory();
  }
}


if(state.currentUser){
  enterApp();
}else{
  loginScreen.style.display = "flex";
  appRoot.style.display = "none";
}

})();