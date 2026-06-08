package dodia.novshield.discordbot.tickets.commands

import dev.minn.jda.ktx.interactions.commands.*
import net.dv8tion.jda.api.Permission
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

import dodia.novshield.discordbot.tickets.listeners.utils.ticketCloseInteraction
import dodia.novshield.discordbot.tickets.listeners.utils.ticketCloseApply
import dodia.novshield.discordbot.tickets.listeners.utils.ticketDeleteInteraction
import dodia.novshield.discordbot.tickets.listeners.utils.ticketDeleteApply
import dodia.novshield.discordbot.tickets.listeners.utils.ticketLogInteraction



object CommandRegister {

    fun registerCommands(jda: JDA) {

        jda.updateCommands {
            slash("управление_участниками", "Управление участниками тикета"){
                restrict(
                    guild = true,
                )
            }
        }.queue()

        CommandsHandler.onCommand(jda)

    }
}