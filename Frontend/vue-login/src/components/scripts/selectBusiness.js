import api from "../../services/api.js";
import { alertDelete } from "../../models/Alerts.js";
import BusinessEdit from "../BusinessEdit.vue";

export function useSelectBusiness() {
  return {
    //Componente externo. Para mostrar el formulario que sirve para editar un negocio
    components: {
      BusinessEdit,
    },
    data() {
      return {
        //Array de empresas que tiene el usuario
        businesses: [],
        loader: false,
        //Variables la logica de botones y titulo.
        buttonTextDelete: "Eliminar Negocio",
        buttonTextEdit: "Editar Negocio",
        isDeleteMode: false,
        isEditMode: false,
        title: "Selecciona Un Negocio",
        isHiddenDelete: false,
        isHiddenEdit: false,
        colorTitle: "black",
        //Variable para mostrar el formulario de edicion de negocios
        showForm: false,
        //Seleccion del negocio
        selectedBusiness: "",
      };
    },
    /**
     * Con el token se comprueba que el usuario pueda entrar a la pagina.
     * Tambien elimina alguna empresa guardada en el sessionStorage
     */
    mounted() {
      const token = sessionStorage.getItem("token");
      sessionStorage.removeItem("business");
      sessionStorage.removeItem("numInvoice");
      
      if (!token) {
        //Devuelve al inicio de la web
        alert("No tienes las acceso a esta pagina");
        this.$router.push("/");
      } else {
        //Obtiene la lista de negocios del usuario
        this.getListBusinesses();
      }
    },

    methods: {
      //Metodo que sirve para obtener la lista de negocios que tiene el usuario.
      async getListBusinesses() {
        this.loader = true;
        try {
          //GET que obtiene la lista, y luego el response se guarda en el array de businesses
          const response = await api.get("/business/get-list");
          this.businesses = response.data;
        } catch (err) {
          console.error("Error obtener lista de negocios:", err);
        } finally {
          this.loader = false;
        }
      },
      //Metodo que ingresa a la pagina para registrar un nuevo negocio
      goRegisterBusiness() {
        this.$router.push("/registerBusiness");
      },

      //Metodo que tiene varias funciones a la vez.
      goMenuOrDelete(businessData) {
        //Si la variable de borrar esta habilitada, invoca el metodo que sirve para eliminar un negocio.
        if (this.isDeleteMode) {
          this.deleteBusiness(businessData);
        }
        //Si la variable de editar esta habilitada, invoca el metodo que abre el formulario de edicicion de un negocio.
        else if (this.isEditMode) {
          this.selectedBusiness = businessData;
          this.formOpenOrClose();
        }
        //En caso que ninguna variable anterior este habilitada, simplemente guarda los datos del negocio seleccionado para futuras operaciones y accede al dashboard
        else {
          //Guarda los datos del negocio en el sessionStorage
          const selectBusiness = {
            idBusiness: businessData.businessId,
            nameBusiness: businessData.nameBusiness,
            cifBusiness: businessData.cifBusiness,
            accountBusiness: businessData.accountNumber,
            addressBusiness: businessData.address,
            emailBusiness: businessData.email,
            phoneBusiness: businessData.phone,
          };
          sessionStorage.setItem("business", JSON.stringify(selectBusiness));
          //Ingresa al dashboard
          this.$router.push("/menu/dashboard");
        }
      },
      //Metodo que sirve para eliminar un negocio seleccionado
      async deleteBusiness(businessData) {
        //Antes de eliminar el negocio, se pide confirmacion
        const result = await alertDelete(
          "¿SEGURO QUE QUIERES ELIMINAR ESTE NEGOCIO?",
          "Vas a eliminar el negocio: " +
            businessData.nameBusiness +
            "(" +
            businessData.cifBusiness +
            ")"
        );

        if (result) {
          try {
            //DELETE con el id del negocio seleccionado, luego se actualiza los datos mostrados
            await api.delete("/business/delete/" + businessData.businessId);
            this.getListBusinesses();
            this.activeDelete();
          } catch (err) {
            console.log("Error Eliminar Negocio: ", err);
          }
        }
      },

      //Metodo que sirve para la logica del boton de eliminacion, que a su vez es el de cancelar.
      activeDelete() {
        this.isHiddenEdit = true;
        if (!this.isDeleteMode && !this.isEditMode) {
          this.isDeleteMode = true;
          this.title = "SELECCIONA UN NEGOCIO PARA QUE SEA ELIMINADO";
          this.buttonTextDelete = "Cancelar";
          this.colorTitle = "#ff4d4d";
        } else {
          this.isHiddenEdit = false;
          this.isDeleteMode = false;
          this.isEditMode = false;
          this.title = "Selecciona Un Negocio";
          this.buttonTextDelete = "Eliminar Negocio";
          this.colorTitle = "black";
        }
      },
      //Metodo que sirve para la logica del boton de edicion.
      activeEdit() {
        this.isHiddenEdit = true;
        if (!this.isEditMode) {
          this.isEditMode = true;
          this.title = "SELECCIONA UN NEGOCIO PARA QUE SEA EDITADO";
          this.buttonTextDelete = "Cancelar";
          this.colorTitle = "#80b3ff";
        } else {
          this.isHiddenDelete = false;
          this.isEditMode = false;
          this.title = "Selecciona Un Negocio";
          this.buttonTextEdit = "Editar Negocio";
          this.colorTitle = "black";
        }
      },

      //Metodo que cierra  el formulario de edicion sin la necesidad de actulizar el listado de negocios.
      formOpenOrClose() {
        if (!this.showForm) {
          this.showForm = true;
        } else {
          this.showForm = false;
        }
      },

      //Metodo que cierra  el formulario de edicion,pero con la actualizacion de la lista de negocios.
      formOpenOrCloseSaved() {
        if (!this.showForm) {
          this.showForm = true;
        } else {
          this.showForm = false;
        }
        this.getListBusinesses();
        this.activeDelete();
      },
      //Meotdo que sirve para regresar el login.
      backLogin() {
        this.$router.push("/");
      },
    },
  };
}
