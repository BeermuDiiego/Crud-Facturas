import api from "../../services/api";
import { CustomChart, CustomMiniChart } from "../../models/Charts";
import FormInvoice from "../FormInvoice.vue";
import FormClient from "../FormClient.vue";

export function useDashboard() {
  return {
    //Componente para mostrar el formulario de crear una nueva factura
    components: { FormInvoice, FormClient },

    data() {
      return {
        //Datos totales para cuadros
        numInvoices: 0,
        numClients: 0,
        invoiceTotal: 0,
        invoiceTotalNoPaid: 0,
        invoiceTotalPaid: 0,
        //Arrays para los tops
        topClients: [],
        topInvoices: [],

        //Botones de cambio de mes
        buttonActive: "buttonDay",
        //Graficos
        miniChartTotal: null,
        miniChartTotalPaid: null,
        miniChartTotalNoPaid: null,
        invoiceChartLine: null,
        invoiceChartBar: null,
        //Datos de facturas para mostrar en la factura
        //Por Dia
        chartDataTotalDay: [],
        chartDataTotalPaidDay: [],
        chartDataTotalNoPaidDay: [],
        chartDataTotalLabelDay: [],
        //Por Mes
        chartDataTotalMonth: [],
        chartDataTotalPaidMonth: [],
        chartDataTotalNoPaidMonth: [],
        chartDataTotalLabelMonth: [],
        //Por anyo
        chartDataTotalYear: [],
        chartDataTotalPaidYear: [],
        chartDataTotalNoPaidYear: [],
        chartDataTotalLabelYear: [],
        //Variable para el cargador de pagina
        loader: false,
        //Variable para mostrar el formulario
        showForm: false,
        showFormClient: false,
      };
    },
    //Al montar la pagina, se comprueba si se tiene el token
    mounted() {
      const token = sessionStorage.getItem("token");
      if (!token) {
        alert("No tienes las acceso a esta pagina");
        this.$router.push("/");
      } else {
        //Se obtiene la lista de facturas
        this.getListInvoices();
      }
    },

    methods: {
      //Metodo para obtener la lista de facturas del negocio
      async getListInvoices() {
        this.loader = true;
        try {
          //A partir del id del negocio, se obtiene la lista total de facturas de la empresa.
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const responseInvoiceTotal = await api.get(
            "/invoice/list-total-prices-business/" + businessData.idBusiness
          );
          //Se guarda los datose en variables para luego mostralo en los cuadros.
          responseInvoiceTotal.data.forEach((invoice) => {
            this.numClients = invoice.numClients;
            this.numInvoices = invoice.numInvoices;
            this.invoiceTotal = invoice.totalPrices;
            this.invoiceTotalNoPaid = invoice.totalPricesNoPaid;
            this.invoiceTotalPaid = invoice.totalPricesPaid;
          });

          //GET que obtiene el top de clientes
          const responseTopClients = await api.get(
            "/invoice/list-total-prices-clients/" + businessData.idBusiness
          );
          this.topClients = responseTopClients.data;
          //GET que obtiene el top de facturas.
          const responseTopInvoices = await api.get(
            "/invoice/list-invoices-total/" + businessData.idBusiness
          );
          this.topInvoices = responseTopInvoices.data;

          this.getLastInvoice(responseTopInvoices.data);

          // GETS para obtener el total de los precios por dia, mes o anyo, para luego asignarlo a los graficos.
          const responseInvoiceTotalDay = await api.get(
            "/invoice/list-total-prices-day-business/" + businessData.idBusiness
          );
          const responseInvoiceTotalMonth = await api.get(
            "/invoice/list-total-prices-month-business/" +
              businessData.idBusiness
          );
          const responseInvoiceTotalYear = await api.get(
            "/invoice/list-total-prices-year-business/" +
              businessData.idBusiness
          );

          this.saveInvoicesData(
            responseInvoiceTotalDay,
            responseInvoiceTotalMonth,
            responseInvoiceTotalYear
          );
          //Se invoca a la creacion de los graficos
          this.createCharts();
        } catch (err) {
          console.error("Error: ", err);
        } finally {
          setTimeout(() => {
            this.loader = false;
          }, 800);
        }
      },

      //Metodo que sirve para guardar los datos obtenidos en sus variables.
      saveInvoicesData(
        responseInvoiceTotalDay,
        responseInvoiceTotalMonth,
        responseInvoiceTotalYear
      ) {
        responseInvoiceTotalDay.data.forEach((invoice) => {
          this.chartDataTotalDay.push(invoice.totalPrices);
          this.chartDataTotalPaidDay.push(invoice.totalPricesPaid);
          this.chartDataTotalNoPaidDay.push(invoice.totalPricesNoPaid);
          this.chartDataTotalLabelDay.push(invoice.invoiceDay);
        });
        responseInvoiceTotalMonth.data.forEach((invoice) => {
          this.chartDataTotalMonth.push(invoice.totalPrices);
          this.chartDataTotalPaidMonth.push(invoice.totalPricesPaid);
          this.chartDataTotalNoPaidMonth.push(invoice.totalPricesNoPaid);
          this.chartDataTotalLabelMonth.push(invoice.invoiceMonth);
        });
        responseInvoiceTotalYear.data.forEach((invoice) => {
          this.chartDataTotalYear.push(invoice.totalPrices);
          this.chartDataTotalPaidYear.push(invoice.totalPricesPaid);
          this.chartDataTotalNoPaidYear.push(invoice.totalPricesNoPaid);
          this.chartDataTotalLabelYear.push(invoice.invoiceYear);
        });
      },

      //Metodo para la creacion de los graficos
      createCharts() {
        //Creacion de los mini graficos para los cuadros
        this.miniChartTotal = new CustomMiniChart(
          "invoiceTotalChart",
          "line",
          this.chartDataTotalDay,
          "65,105,225",
          this.chartDataTotalLabelDay
        );

        this.miniChartTotalPaid = new CustomMiniChart(
          "invoiceTotalPaidChart",
          "line",
          this.chartDataTotalPaidDay,
          "50,205,50",
          this.chartDataTotalLabelDay
        );

        this.miniChartTotalNoPaid = new CustomMiniChart(
          "invoiceTotalNoPaidChart",
          "line",
          this.chartDataTotalNoPaidDay,
          "255,0,0",
          this.chartDataTotalLabelDay
        );

        //Creacion de los  graficos grandes
        this.invoiceChartLine = new CustomChart(
          "invoiceChartLine",
          "line",
          this.chartDataTotalDay,
          this.chartDataTotalPaidDay,
          this.chartDataTotalNoPaidDay,
          this.chartDataTotalLabelDay
        );
        this.invoiceChartBar = new CustomChart(
          "invoiceChartBar",
          "bar",
          this.chartDataTotalDay,
          this.chartDataTotalPaidDay,
          this.chartDataTotalNoPaidDay,
          this.chartDataTotalLabelDay
        );
      },

      //Metodo que se usa para la logica de los botones de dia, mes y anyo.
      checkButtons(button) {
        if (button === "buttonDay" && this.buttonActive != "buttonDay") {
          this.setActiveButtonAndCharts(
            button,
            this.chartDataTotalDay,
            this.chartDataTotalNoPaidDay,
            this.chartDataTotalPaidDay,
            this.chartDataTotalLabelDay
          );
        } else if (
          button === "buttonMonth" &&
          this.buttonActive != "buttonMonth"
        ) {
          this.setActiveButtonAndCharts(
            button,
            this.chartDataTotalMonth,
            this.chartDataTotalNoPaidMonth,
            this.chartDataTotalPaidMonth,
            this.chartDataTotalLabelMonth
          );
        } else if (
          button === "buttonYear" &&
          this.buttonActive != "buttonYear"
        ) {
          this.setActiveButtonAndCharts(
            button,
            this.chartDataTotalYear,
            this.chartDataTotalNoPaidYear,
            this.chartDataTotalPaidYear,
            this.chartDataTotalLabelYear
          );
        }
      },
      //Metodo para mostrar el formulario de crear factura sin actulizar datos
      formOpenOrClose() {
        if (!this.showForm) {
          this.showForm = true;
        } else {
          this.showForm = false;
        }
      },
      //Metodo para mostrar el formulario de crear cliente sin actulizar datos
      formOpenOrCloseClient() {
        if (!this.showFormClient) {
          this.showFormClient = true;
        } else {
          this.showFormClient = false;
        }
      },
      //Metodo para mostrar el formulario de crear factura actualizando datos
      formOpenOrCloseSaved() {
        if (!this.showForm) {
          this.showForm = true;
        } else {
          this.showForm = false;
          setTimeout(() => {
            this.$router.go(0);
          }, 1100);
        }
      },
      //Metodo para mostrar el formulario de crear cliente actualizando datos
      formOpenOrCloseClientSaved() {
        if (!this.showFormClient) {
          this.showFormClient = true;
        } else {
          this.showFormClient = false;
          setTimeout(() => {
            this.$router.go(0);
          }, 1100);
        }
      },

      //Meotodo que sirve para obtener el ultimo numero de factura.
      getLastInvoice(numLastInvoices) {
        let lastNum = 0;
        let lastNumYear = 0;
        let latestInvoice = 0;
        numLastInvoices.map((invoice) => {
          let invoiceNum = parseInt(invoice.numInvoice.split("-")[0]);
          let invoiceNumYear = parseInt(invoice.numInvoice.split("-")[1]);

          // Se comprueba si es el mayor numero de factura
          if (
            invoiceNumYear > lastNumYear ||
            (invoiceNumYear === lastNumYear && invoiceNum > lastNum)
          ) {
            lastNumYear = invoiceNumYear;
            lastNum = invoiceNum;
            latestInvoice = invoice.numInvoice;
          }
        });
        sessionStorage.setItem("numInvoice", latestInvoice);
      },

      //Metodo que activa el boton, y actualiza los datos de las facturas que se pasa por parametro.
      setActiveButtonAndCharts(
        button,
        dataTotal,
        dataTotalNoPaid,
        dataTotalPaid,
        dataLabel
      ) {
        this.loader = true;
        //Cambia el boton activo (dia, mes o anyo)
        this.buttonActive = button;
        //Actualiza los graficos.
        try {
          this.miniChartTotal.updateMiniChart(dataTotal, dataLabel);
          this.miniChartTotalNoPaid.updateMiniChart(dataTotalNoPaid, dataLabel);
          this.miniChartTotalPaid.updateMiniChart(dataTotalPaid, dataLabel);
          this.invoiceChartLine.updateChart(
            dataTotal,
            dataTotalPaid,
            dataTotalNoPaid,
            dataLabel
          );
          this.invoiceChartBar.updateChart(
            dataTotal,
            dataTotalPaid,
            dataTotalNoPaid,
            dataLabel
          );
        } catch (error) {
          console.log(error);
        } finally {
          setTimeout(() => {
            this.loader = false;
          }, 900);
        }
      },
    },
  };
}
