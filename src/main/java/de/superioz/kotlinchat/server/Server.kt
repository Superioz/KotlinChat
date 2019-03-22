package de.superioz.kotlinchat.server

import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset


/**
 * @author Tobias Büser
 */
fun main(args: Array<String>) {
    // startup server, thats basically it

    val server = ServerSocket(13378)
    println("Server socket bound to localhost:${server.localPort}")

    val clients = mutableListOf<Socket>()

    while(true) {
        println("Waiting for connection ..")
        val socket = server.accept()
        println("Client connected. (${socket.inetAddress.hostAddress})")

        clients.add(socket)

        Thread {
            try {
                val streamReader = InputStreamReader(socket.getInputStream())

                streamReader.forEachLine {
                    val id = clients.indexOf(socket)
                    val message = "$id says: $it"

                    println(message)

                    clients.forEach {s ->
                        s.getOutputStream().write((message + "\n").toByteArray(Charset.forName("UTF-8")))
                        s.getOutputStream().flush()
                    }
                }
            } catch (ex: SocketException) {
                println("Client disconnected")

                clients.remove(socket)
            }
        }.start()
    }
}
