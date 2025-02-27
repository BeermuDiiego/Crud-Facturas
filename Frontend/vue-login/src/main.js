import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { AllCommunityModule, ModuleRegistry } from "ag-grid-community";

// Register all Community features
ModuleRegistry.registerModules([AllCommunityModule]);

createApp(App).use(router).mount("#app");
