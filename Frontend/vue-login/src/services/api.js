import axios from "axios";

// Configurar Axios
const api = axios.create({
  baseURL: "http://localhost:8080", // Dirección de tu API
  headers: {
    "Content-Type": "application/json",
  },
});

// Agregar el token de autenticación a cada solicitud
api.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
