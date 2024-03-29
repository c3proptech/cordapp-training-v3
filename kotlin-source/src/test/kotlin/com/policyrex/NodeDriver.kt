package com.policyrex

import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.driver
import net.corda.testing.node.User

fun main(args: Array<String>) {
    val user = User("user1", "test", permissions = setOf("ALL"))
    driver(DriverParameters(waitForAllNodesToFinish = true)) {
        val nodeFutures = listOf(
                startNode(
                        providedName = CordaX500Name("User", "Beijing Shi", "CN"),
                        customOverrides = mapOf("rpcSettings.address" to "localhost:10008", "rpcSettings.adminAddress" to "localhost:10048", "webAddress" to "localhost:10009"),
                        rpcUsers = listOf(user)),
                startNode(
                        providedName = CordaX500Name("Insurer", "New York", "US"),
                        customOverrides = mapOf("rpcSettings.address" to "localhost:10011", "rpcSettings.adminAddress" to "localhost:10051", "webAddress" to "localhost:10012"),
                        rpcUsers = listOf(user)),
                startNode(
                        providedName = CordaX500Name("PolicyREX", "Toronto", "CA"),
                        customOverrides = mapOf("rpcSettings.address" to "localhost:10014", "rpcSettings.adminAddress" to "localhost:10054", "webAddress" to "localhost:10015"),
                        rpcUsers = listOf(user)))

        val (nodeA, nodeB, nodeC) = nodeFutures.map { it.getOrThrow() }

        startWebserver(nodeA)
        startWebserver(nodeB)
        startWebserver(nodeC)
    }
}
