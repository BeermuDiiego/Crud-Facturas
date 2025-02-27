import { Chart, registerables } from "chart.js";

//Clase para crear un grafico
export class CustomChart {
  constructor(
    chartId,
    type,
    dataTotal = [],
    dataTotalPaid = [],
    dataTotalNoPaid = [],
    labels = []
  ) {
    Chart.register(...registerables);
    this.chartId = chartId;
    this.type = type;
    this.dataTotal = dataTotal;
    this.dataTotalPaid = dataTotalPaid;
    this.dataTotalNoPaid = dataTotalNoPaid;
    this.labels = labels;
    this.chart = null;
    this.createChart();
  }

  //Metodo que guarda los datos para el grafico
  createChart() {
    const chartTemplate = document.getElementById(this.chartId);

    const dataChart = {
      labels: this.labels,
      datasets: [
        {
          data: this.dataTotal,
          label: "Total",
          borderColor: "rgb(65,105,225)",
          backgroundColor: "rgb(65,105,225,0.9)",
          borderWidth: 2,
          tension: 0,
        },
        {
          data: this.dataTotalPaid,
          label: "Total Pagado",
          borderColor: "rgb(50,205,50)",
          backgroundColor: "rgb(50,205,50,0.9)",
          borderWidth: 2,
          tension: 0,
        },
        {
          data: this.dataTotalNoPaid,
          label: "Total No Pagado",
          borderColor: "rgb(255,0,0)",
          backgroundColor: "rgb(255,0,0,0.9)",
          borderWidth: 2,
          tension: 0,
        },
      ],
    };

    //Configuracion que tendra el grafico
    const optionsChart = {
      responsive: true,
      maintainAspectRatio: true,
      aspectRatio: 2.5,
      plugins: {
        tooltip: {
          callbacks: {
            label: function (context) {
              return `${context.raw}€`;
            },
          },
        },
        legend: {
          display: true,
        },
      },
      scales: {
        y: {
          ticks: {
            callback: function (value) {
              return `${value}€`;
            },
          },
        },
      },
    };

    //Crea y guarda en la variable el nuevo grafico.
    this.chart = new Chart(chartTemplate, {
      type: this.type,
      data: dataChart,
      options: optionsChart,
    });
  }

  //Actualiza el grafico con los datos pasados por parametro.
  updateChart(newDataTotal, newDataTotalPaid, newDataTotalNoPaid, newLabels) {
    this.chart.destroy();
    this.dataTotal = newDataTotal;
    this.dataTotalPaid = newDataTotalPaid;
    this.dataTotalNoPaid = newDataTotalNoPaid;
    this.labels = newLabels;
    this.createChart();
  }
}

//Clase para crear un mini grafico
export class CustomMiniChart {
  constructor(chartId, type, data = [], colorData, labels = []) {
    Chart.register(...registerables);
    this.chartId = chartId;
    this.type = type;
    this.data = data;
    this.colorData = colorData;
    this.labels = labels;
    this.chart = null;
    this.createMiniChart();
  }

  //Metodo que guarda los datos para el grafico
  createMiniChart() {
    const miniChartTemplate = document.getElementById(this.chartId);

    const dataChart = {
      labels: this.labels,
      datasets: [
        {
          data: this.data,
          borderColor: ["rgb(" + this.colorData + ")"],
          backgroundColor: ["rgb(" + this.colorData + ",0.2)"],
          borderWidth: 1,
          tension: 0.3,
          fill: true,
        },
      ],
    };

    //Configuracion que tendra el grafico
    const optionsChart = {
      responsive: true,
      maintainAspectRatio: true,
      aspectRatio: 4,
      plugins: {
        tooltip: {
          callbacks: {
            label: function (context) {
              return `${context.raw}€`;
            },
          },
        },
        legend: {
          display: false,
        },
      },
      scales: {
        x: {
          ticks: {
            display: false,
          },
          grid: {
            drawTicks: false,
          },
        },

        y: {
          ticks: {
            display: false,
          },
          grid: {
            drawTicks: false,
          },
        },
      },
      elements: {
        point: {
          radius: 1.5,
        },
      },
    };
    //Creacion del mini grafico
    this.chart = new Chart(miniChartTemplate, {
      type: this.type,
      data: dataChart,
      options: optionsChart,
    });
  }

  //Metodo que actualiza el mini grafico
  updateMiniChart(newData, newLabels) {
    this.chart.destroy();
    this.data = newData;
    this.labels = newLabels;
    this.createMiniChart();
  }
}
