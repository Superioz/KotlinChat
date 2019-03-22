package de.superioz.kotlinchat.client

import java.io.BufferedOutputStream
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset
import java.util.*


/**
 * @author Tobias Büser
 */
fun main(args: Array<String>) {
    // start client and connect to hardcoded server
    val socket = Socket("localhost", 13378)

    println("Connected to server.")

    Thread {
        while(true) {
            try {
                val streamReader = InputStreamReader(socket.getInputStream())

                streamReader.forEachLine {
                    println(it)
                }
            } catch (ex: SocketException) {
                println("Disconnected from server")
                System.exit(0)
            }
        }
    }.start()

    println("Waiting for input ..")

    val consoleReader = Scanner(System.`in`)
    while(consoleReader.hasNext()) {
        val message = consoleReader.nextLine()

        println(message)
        try {
            val output = BufferedOutputStream(socket.getOutputStream())
            output.write((message + System.lineSeparator()).toByteArray(Charset.forName("UTF-8")))
            output.flush()
        } catch(ex: SocketException) {
            println("Disconnected from server.")
            System.exit(0)
        }
    }

}
