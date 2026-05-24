package dodia.novshield.discordbot.tickets.listeners

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

import dodia.novshield.discordbot.tickets.listeners.utils.ticketCloseInteraction
import dodia.novshield.discordbot.tickets.listeners.utils.ticketCloseApply
import dodia.novshield.discordbot.tickets.listeners.utils.ticketDeleteInteraction
import dodia.novshield.discordbot.tickets.listeners.utils.ticketDeleteApply
import dodia.novshield.discordbot.tickets.listeners.utils.ticketLogInteraction


fun buttonListener(jda: JDA) {

    jda.listener<ButtonInteractionEvent> { event ->

        when (event.component.customId) {


            "btn_close" -> {

                ticketCloseInteraction(event)
                event.deferEdit().queue()
                return@listener
            }

            "btn_apply_close" -> {

                ticketCloseApply(event)
                event.deferEdit().queue()
                return@listener

            }

            "btn_delete" -> {

                ticketDeleteInteraction(event)
                event.deferEdit().queue()
                return@listener

            }

            "btn_apply_delete" -> {

                ticketDeleteApply(event)
                event.deferEdit().queue()
                return@listener


            }

            "btn_log" -> {
                ticketLogInteraction(event)
                event.deferEdit().queue()
                return@listener
            }


        }

    }

}