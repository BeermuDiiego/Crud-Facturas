<template>
  <div v-if="visible" class="modal-overlay">
    <!--Formulario para crear una nueva factura-->
    <div class="form-invoice-container">
      <h1>Crear Nueva Factura</h1>
      <br />
      <!--Boton para cerrar el formulario-->
      <button class="close-button" @click="closeForm1">✖</button>
      <div class="data-container">
        <div class="client-data">
          <!--Seleccion de clientes existentes-->
          <h2>
            Cliente:
            <select v-model="selectedClient">
              <option disabled>Selecciona un cliente</option>
              <option
                v-for="cliente in clientes"
                :key="cliente.clientId"
                :value="cliente.clientId"
              >
                {{ cliente.nameClient }}
              </option>
            </select>
          </h2>
          <!--Cif de cliente-->
          <h2>CIF: {{ clientCIF }}</h2>
          <!--Direccion del cliente-->
          <h2>Direccion: {{ clientAddress }}</h2>
        </div>
        <div class="invoice-data">
          <!--Numero de factura-->
          <h2>Nº Factura: {{ newNumInvoice }}</h2>
          <!--Fecha de la factura-->
          <h2>Fecha: <input v-model="dateInvoice" type="date" required /></h2>
          <!--Referencia de la factura-->
          <h2>
            Referencia:
            <input
              v-model="referenceInvoice"
              type="text"
              placeholder="Indica una referencia para la factura"
            />
          </h2>
        </div>
      </div>
      <!-- Tabla de items para la factura -->
      <div class="table-form-container">
        <table class="form-table">
          <thead>
            <tr>
              <th>Servicio</th>
              <th>Cantidad</th>
              <th>Precio Unitario</th>
              <th>Importe Total</th>
              <th v-if="rows.length > 1"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in rows" :key="index">
              <td>
                <textarea
                  v-model="row.serviceName"
                  placeholder="Descripcion del trabajo/producto"
                  rows="3"
                  style="resize: none; width: 100%"
                ></textarea>
              </td>
              <td>
                <input
                  type="number"
                  v-model="row.quantity"
                  placeholder="Cantidad del trabajo/producto"
                />
              </td>
              <td>
                <input
                  type="number"
                  v-model="row.price"
                  placeholder="Precio por unidad"
                />
              </td>
              <td>
                <h2>{{ row.quantity * row.price }}€</h2>
              </td>
              <td v-if="rows.length > 1">
                <!--Boton para eliminar una fila-->
                <button
                  class="button-delete"
                  v-if="rows.length > 1"
                  @click="removeRow(index)"
                >
                  ✖
                </button>
              </td>
            </tr>
            <tr>
              <!--Boton para agregar filas-->
              <td colspan="5">
                <button class="button-add" @click="addRow">Agregar Fila</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="totals-container">
        <!--Checkbox de pagado o no apagado-->
        <div>
          <input type="checkbox" id="checkbox" v-model="checkPaid" />
          <label for="checkbox">{{ checkPaid ? "Pagado" : "No Pagado" }}</label>
        </div>
        <div>
          <!--Subtotal-->
          <h2>Subtotal: {{ subtotal }}€</h2>
        </div>
        <div>
          <!--IVA-->
          <h2>
            IVA:
            <input class="input-iva" type="number" v-model="iva.porcentaje" />%
          </h2>
        </div>
        <div>
          <!--Total-->
          <h2>Total: {{ total }}€</h2>
        </div>
      </div>
      <!--Botones de guardar o cancelar-->
      <div class="buttons-container">
        <div class="button-save" @click="saveData">Guardar</div>
        <div class="button-cancel" @click="closeForm1">Cancelar</div>
      </div>
    </div>
  </div>
</template>

<script>
import { UseFormInvoice } from "./scripts/formInvoiceScript";
export default {
  ...UseFormInvoice(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/form-invoice.css");
</style>
