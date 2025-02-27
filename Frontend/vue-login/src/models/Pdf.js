import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";

function createPdf(invoiceData) {
  try {
    // Crear una nueva instancia de jsPDF
    const doc = new jsPDF();
    const businessData = JSON.parse(sessionStorage.getItem("business"));

    // Cabecera
    doc.setFontSize(30);
    doc.setTextColor(0, 51, 153); // Azul
    doc.text(businessData.nameBusiness, 14, 20);

    // Fuente y color para datos
    doc.setFontSize(12);
    doc.setTextColor(0, 0, 0);
    // Dirección
    doc.setFont("helvetica", "bold");
    doc.text("Dirección: ", 14, 30);
    doc.setFont("helvetica", "normal");
    doc.text(businessData.addressBusiness, 35, 30);

    // Correo Electrónico
    doc.setFont("helvetica", "bold");
    doc.text("Correo Electrónico: ", 14, 35);
    doc.setFont("helvetica", "normal");
    doc.text(businessData.emailBusiness, 54, 35);

    // CIF/DNI
    doc.setFont("helvetica", "bold");
    doc.text("CIF/DNI: ", 14, 40);
    doc.setFont("helvetica", "normal");
    doc.text(businessData.cifBusiness, 31, 40);

    // Teléfono
    doc.setFont("helvetica", "bold");
    doc.text("Teléfono: ", 14, 45);
    doc.setFont("helvetica", "normal");
    doc.text(businessData.phoneBusiness, 33, 45);

    // Cuenta Bancaria
    doc.setFont("helvetica", "bold");
    doc.text("Cuenta Bancaria: ", 14, 50);
    doc.setFont("helvetica", "normal");
    doc.text(businessData.accountBusiness, 49, 50);

    // Linea separacion
    doc.setLineWidth(0.5);
    doc.line(14, 55, 195, 55);
    doc.setTextColor(0, 0, 0);

    // Detalles de la factura
    doc.setFontSize(20);
    doc.setFont("helvetica", "bold");
    doc.text("FACTURA", 140, 65);

    // Datos de la factura
    doc.setFontSize(12);
    doc.text("Nº DE FACTURA: ", 140, 75);
    doc.setFont("helvetica", "normal");
    doc.text(invoiceData.num, 175, 75);

    doc.setFont("helvetica", "bold");
    doc.text("FECHA: ", 140, 81);
    doc.setFont("helvetica", "normal");
    doc.text(invoiceData.date, 157, 81);
    doc.setFont("helvetica", "bold");
    doc.text("Referencia: ", 140, 87);
    doc.setFont("helvetica", "normal");
    doc.text(invoiceData.address, 140, 92);

    // Detalles del cliente
    doc.setTextColor(0, 51, 153);
    doc.setFontSize(15);
    doc.setFont("helvetica", "bold");
    doc.text("FACTURAR A", 14, 70);
    doc.setTextColor(0, 0, 0); // Negro
    doc.setFontSize(15);
    doc.setFont("helvetica", "bold");
    doc.text(invoiceData.clients.nameClient, 14, 76);
    doc.setFontSize(12);
    doc.setFont("helvetica", "bold");
    doc.text("NIF/CIF: ", 14, 83);
    doc.setFont("helvetica", "normal");
    doc.text(invoiceData.clients.cifClient, 31, 83);
    doc.setFont("helvetica", "bold");
    doc.text("Dirección: ", 14, 89);
    doc.setFont("helvetica", "normal");
    doc.text(invoiceData.clients.address, 14, 94);

    // Tabla de productos (valores del parámetro invoiceData)
    const columns = [
      { header: "Descripcion", dataKey: "description" },
      { header: "Cantidad", dataKey: "quantity" },
      { header: "Precio Unitario", dataKey: "unitPrice" },
      { header: "Precio Total", dataKey: "totalPrice" },
    ];

    // Accede correctamente a invoiceData.invoicesItems
    const rows = invoiceData.invoicesItems.map((item) => ({
      description: item.serviceName,
      quantity: item.quantity,
      unitPrice: item.price.toFixed(2) + " €",
      totalPrice: item.total.toFixed(2) + " €",
    }));

    // Genera la tabla
    autoTable(doc, {
      startY: 105, // Punto de inicio en Y
      head: [columns.map((col) => col.header)], // Cabecera de la tabla
      body: rows.map((row) => [
        row.description,
        row.quantity,
        row.unitPrice,
        row.totalPrice,
      ]),
    });
    // Detalles del total
    doc.setTextColor(0, 0, 0);
    doc.setFont("helvetica", "bold");
    doc.setFontSize(15);
    //Subtotal
    doc.text("Subtotal: ", 140, doc.lastAutoTable.finalY + 20);
    doc.setFont("helvetica", "normal");
    doc.setFontSize(12);
    doc.text(
      invoiceData.subtotalInvoice.toFixed(2) + " €",
      164,
      doc.lastAutoTable.finalY + 20
    );
    //IVA
    doc.setFont("helvetica", "bold");
    doc.setFontSize(15);
    doc.text("IVA: ", 140, doc.lastAutoTable.finalY + 27);
    doc.setFont("helvetica", "normal");
    doc.setFontSize(12);
    doc.text(
      invoiceData.iva.toFixed(2) + "%",
      152,
      doc.lastAutoTable.finalY + 27
    );
    //Total
    doc.setFont("helvetica", "bold");
    doc.setFontSize(15);
    doc.text("TOTAL: ", 140, doc.lastAutoTable.finalY + 34);
    doc.setFont("helvetica", "normal");
    doc.setFontSize(12);
    doc.text(
      invoiceData.total.toFixed(2) + " €",
      160,
      doc.lastAutoTable.finalY + 34
    );

    // Generar el PDF
    const pdfOutput = doc.output("blob");

    // Crear una URL para el blob
    const pdfUrl = URL.createObjectURL(pdfOutput);

    // Abrir el PDF en una nueva ventana
    window.open(pdfUrl, "_blank");
  } catch (err) {
    console.log("Error crear pdf: ", err);
  }
}

export { createPdf };
