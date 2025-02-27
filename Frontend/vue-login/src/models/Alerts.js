import Swal from "sweetalert2";

//Funcion de alerta de borrado
function alertDelete(titleAlert, textAlert) {
  return Swal.fire({
    title: titleAlert,
    text: textAlert,
    icon: "warning",
    showDenyButton: true,
    confirmButtonText: "Borrar",
    denyButtonText: "No Borrar",
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire({
        title: "Borrado",
        timer: 2200,
        icon: "success",
        showConfirmButton: false,
      });
      return true;
    } else if (result.isDenied) {
      Swal.fire({
        title: "No Borrado",
        timer: 1200,
        icon: "info",
        showConfirmButton: false,
      });
      return false;
    }
  });
}
//Funcion de alerta de informacion
function alertInfo(titleInfo, textInfo) {
  return Swal.fire({
    title: titleInfo,
    text: textInfo,
    icon: "info",
    draggable: true,
    timer: 2300,
  });
}
//Funcion de alerta de exito
function alertSuccess(titleSuccess) {
  return Swal.fire({
    title: titleSuccess,
    icon: "success",
    draggable: true,
    showConfirmButton: false,
    timer: 2300,
  });
}

export { alertDelete, alertInfo, alertSuccess };
