window.onload = function() {
    var inputPrecio = document.getElementById('precio') || document.querySelector('input[name="precio"]');

    if (inputPrecio) {
        inputPrecio.oninput = function() {
            // Limpia la restriccion nativa al teclear para que acepte cualquier numero nuevo
            inputPrecio.setCustomValidity("");

            var precio = parseFloat(inputPrecio.value);
            var mensaje15 = document.getElementById("mensaje15");

            if (mensaje15) {
                if (precio > 0) {
                    var comision = (precio * 0.15).toFixed(2);
                    var ganancia = (precio * 0.85).toFixed(2);
                    mensaje15.innerHTML = "Se aplicará el 15% de comisión ($" + comision + "). Recibirás: $" + ganancia + " MXN";
                } else {
                    mensaje15.innerHTML = "";
                }
            }
        };
    }
};