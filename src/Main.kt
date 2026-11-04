import kotlin.system.exitProcess

data class Contacto(val nombre: String, val telefono: String, val email: String)

// Lista donde se almacenan los contactos y se mantiene ordenada por nombre
val contactos: MutableList<Contacto> = mutableListOf()

fun leerLinea(mensaje: String): String {
    print(mensaje)
    // Si se cierra la entrada estándar se sale del programa
    return readlnOrNull()?.trim() ?: exitProcess(0)
}

fun leerObligatorio(mensaje: String): String {
    while (true) {
        val texto = leerLinea(mensaje)
        if (texto.isNotEmpty()) return texto
        println("Error: este campo es obligatorio y no puede estar vacío.")
    }
}

// Pide un teléfono y repite hasta que tenga 9 dígitos sin letras ni símbolos
fun leerTelefono(mensaje: String): String {
    val formatoValido = Regex("[0-9]{9}")
    while (true) {
        val texto = leerLinea(mensaje)
        if (formatoValido.matches(texto)) return texto
        println("Error: el teléfono debe tener exactamente 9 números, sin letras ni espacios.")
    }
}

fun mostrarMenu() {
    println()
    println("===== AGENDA DE CONTACTOS =====")
    println("1. Añadir contacto")
    println("2. Listar contactos")
    println("3. Buscar contacto")
    println("4. Eliminar contacto")
    println("5. Salir")
}

fun anadirContacto() {
    val nombre = leerObligatorio("Introduce el nombre: ")
    val telefono = leerTelefono("Introduce el teléfono: ")
    val email = leerObligatorio("Introduce el correo electrónico: ")

    contactos.add(Contacto(nombre, telefono, email))
    contactos.sortBy { it.nombre.lowercase() }
    println("Contacto añadido correctamente.")
}

fun imprimirContacto(posicion: Int, c: Contacto) {
    println("$posicion. ${c.nombre} - ${c.telefono} - ${c.email}")
}

fun listarContactos() {
    if (contactos.isEmpty()) {
        println("La agenda no contiene contactos.")
        return
    }
    contactos.forEachIndexed { i, c -> imprimirContacto(i + 1, c) }
}

fun buscarContacto() {
    if (contactos.isEmpty()) {
        println("La agenda no contiene contactos.")
        return
    }
    val texto = leerObligatorio("Introduce el nombre a buscar: ")
    val resultados = contactos.filter { it.nombre.contains(texto, ignoreCase = true) }

    if (resultados.isEmpty()) {
        println("No se han encontrado contactos con ese nombre.")
    } else {
        resultados.forEachIndexed { i, c -> imprimirContacto(i + 1, c) }
    }
}

fun eliminarContacto() {
    if (contactos.isEmpty()) {
        println("La agenda no contiene contactos.")
        return
    }
    listarContactos()
    val entrada = leerObligatorio("Introduce el número o el nombre del contacto a eliminar: ")
    val posicion = entrada.toIntOrNull()

    if (posicion != null) {
        // Eliminación por posición
        if (posicion in 1..contactos.size) {
            contactos.removeAt(posicion - 1)
            println("Contacto eliminado correctamente.")
        } else {
            println("Error: no existe ningún contacto en la posición $posicion.")
        }
    } else {
        // Eliminación por nombre exacto sin distinguir mayúsculas
        val eliminado = contactos.removeAll { it.nombre.equals(entrada, ignoreCase = true) }
        if (eliminado) {
            println("Contacto eliminado correctamente.")
        } else {
            println("Error: no existe ningún contacto con el nombre \"$entrada\".")
        }
    }
}

fun main() {
    var salir = false
    while (!salir) {
        mostrarMenu()
        val opcion = leerLinea("Seleccione una opción: ").toIntOrNull()
        when (opcion) {
            1 -> anadirContacto()
            2 -> listarContactos()
            3 -> buscarContacto()
            4 -> eliminarContacto()
            5 -> {
                println("Saliendo de la agenda. ¡Hasta pronto!")
                salir = true
            }
            else -> println("Opción no válida. Introduce un número del 1 al 5.")
        }
    }
}