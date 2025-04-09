import React, { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({ name: "", email: "" });
  const [editingUserId, setEditingUserId] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = () => {
    fetch("http://localhost:8080/api/users")
      .then((response) => response.json())
      .then((data) => setUsers(data))
      .catch((error) =>
        console.error("Erro a encontrar utilizadores: ", error)
      );
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewUser((prev) => ({ ...prev, [name]: value }));
  };

  const addUser = () => {
    fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newUser),
    }).then((res) => {
      if (res.ok) {
        setNewUser({ name: "", email: "" });
        fetchUsers();
      } else {
        alert("Erro ao adicionar utilizador!");
      }
    });
  };

  const handleUserChange = (id, field, value) => {
    setUsers(
      users.map((user) => (user.id === id ? { ...user, [field]: value } : user))
    );
  };

  const saveUser = (user) => {
    fetch(`http://localhost:8080/api/users/${user.id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: user.name,
        email: user.email,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Usuário atualizado:", data);
        fetchUsers(); // Atualiza a lista de utilizadores após a edição
        setEditingUserId(null); // Sai do modo de edição
      })
      .catch((error) => console.error("Erro ao atualizar usuário:", error));
  };

  const deleteUser = (id) => {
    fetch(`http://localhost:8080/api/users/${id}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (res.ok) {
          // Remove o utilizador da lista sem precisar de recarregar a página
          setUsers(users.filter((user) => user.id !== id));
        } else {
          alert("Erro ao eliminar utilizador!");
        }
      })
      .catch((error) => console.error("Erro ao eliminar utilizador:", error));
  };

  return (
    <div className="container">
      <h1>Lista de Utilizadores</h1>

      {/* Formulário para adicionar novo utilizador */}
      <div className="add-user-form">
        <input
          type="text"
          name="name"
          placeholder="Nome"
          value={newUser.name}
          onChange={handleChange}
        />
        <input
          type="email"
          name="email"
          placeholder="E-mail"
          value={newUser.email}
          onChange={handleChange}
        />
        <button className="button" onClick={addUser}>
          Adicionar Utilizador
        </button>
      </div>

      {/* Exibe lista de utilizadores */}
      {users.length === 0 ? (
        <p>Nenhum utilizador encontrado.</p>
      ) : (
        <ul className="user-list">
          {users.map((user) => (
            <li className="user-card" key={user.id}>
              {editingUserId === user.id ? (
                <div className="edit-user-form">
                  <input
                    type="text"
                    value={user.name}
                    onChange={(e) =>
                      handleUserChange(user.id, "name", e.target.value)
                    }
                  />
                  <input
                    type="email"
                    value={user.email}
                    onChange={(e) =>
                      handleUserChange(user.id, "email", e.target.value)
                    }
                  />
                  <button
                    className="button-save"
                    onClick={() => saveUser(user)}
                  >
                    Guardar
                  </button>
                </div>
              ) : (
                <>
                  <div>
                    <strong>{user.name}</strong>
                    <span>- {user.email}</span>
                  </div>

                  <div className="buttons-container">
                    <button
                      className="edit"
                      onClick={() => setEditingUserId(user.id)}
                    >
                      Editar
                    </button>
                    <button
                      className="delete"
                      onClick={() => deleteUser(user.id)}
                    >
                      Eliminar
                    </button>
                  </div>
                </>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;
