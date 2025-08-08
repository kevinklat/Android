package com.meusprojetos.minhasferramentas

data class Tool(
    val name: String,
    val description: String,
    val icon: Int,
    val activity: Class<*>
)

