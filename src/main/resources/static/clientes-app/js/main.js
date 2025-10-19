const API_URL = '/api/cliente';

async function listarClientes() {
  const response = await fetch(`${API_URL}/listar`);
  const clientes = await response.json();

  const tabla = document.getElementById('tablaClientes');
  tabla.innerHTML = '';

  clientes.forEach(c => {
    const fila = document.createElement('tr');
    fila.innerHTML = `
      <td>${c.id}</td>
      <td>${c.nombre}</td>
      <td>${c.dni}</td>
      <td>${c.telefono}</td>
      <td>${c.direccion}</td>
      <td>${c.fechaCreacion ? c.fechaCreacion : ''}</td>
      <td>
        <button class="btn btn-sm btn-warning me-2" onclick="editarCliente(${c.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="eliminarCliente(${c.id})">Eliminar</button>
      </td>
    `;
    tabla.appendChild(fila);
  });
}

async function crearCliente() {
  const cliente = {
    nombre: document.getElementById('nombre').value,
    dni: document.getElementById('dni').value,
    telefono: document.getElementById('telefono').value,
    direccion: document.getElementById('direccion').value,
    fechaCreacion: document.getElementById('fechaCreacion').value
  };

  await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(cliente)
  });

  limpiarFormulario();
  listarClientes();
}

async function editarCliente(id) {
  const response = await fetch(`${API_URL}/${id}`);
  const cliente = await response.json();

  document.getElementById('nombre').value = cliente.nombre;
  document.getElementById('dni').value = cliente.dni;
  document.getElementById('telefono').value = cliente.telefono;
  document.getElementById('direccion').value = cliente.direccion;
  document.getElementById('fechaCreacion').value = cliente.fechaCreacion || '';

  // Reemplaza el botón "Guardar" por "Actualizar"
  const btn = document.querySelector('button.btn-primary');
  btn.textContent = 'Actualizar';
  btn.onclick = () => actualizarCliente(id);
}

async function actualizarCliente(id) {
  const cliente = {
    nombre: document.getElementById('nombre').value,
    dni: document.getElementById('dni').value,
    telefono: document.getElementById('telefono').value,
    direccion: document.getElementById('direccion').value,
    fechaCreacion: document.getElementById('fechaCreacion').value
  };

  await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(cliente)
  });

  limpiarFormulario();
  listarClientes();
}

async function eliminarCliente(id) {
  if (confirm('¿Seguro que deseas eliminar este cliente?')) {
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    listarClientes();
  }
}

function limpiarFormulario() {
  document.getElementById('nombre').value = '';
  document.getElementById('dni').value = '';
  document.getElementById('telefono').value = '';
  document.getElementById('direccion').value = '';
  document.getElementById('fechaCreacion').value = '';

  const btn = document.querySelector('button.btn-primary');
  btn.textContent = 'Guardar';
  btn.onclick = crearCliente;
}

document.addEventListener('DOMContentLoaded', listarClientes);

