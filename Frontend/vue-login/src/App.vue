<template>
  <div id="app">
    <router-view></router-view>
    <!-- Aquí se renderizan las vistas según la ruta -->
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}
</style>
