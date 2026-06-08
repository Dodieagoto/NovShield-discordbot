package dodia.novshield.discordbot.tickets.listeners



import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

import dodia.novshield.discordbot.tickets.commands.verdict.VerdictModalHandler
import dodia.novshield.discordbot.tickets.panels.Court
import dodia.novshield.discordbot.tickets.panels.ModerComplaint
import dodia.novshield.discordbot.tickets.panels.ParliamentQuestion
import dodia.novshield.discordbot.tickets.panels.Patent
import dodia.novshield.discordbot.tickets.panels.TechnicalQuestion
import dodia.novshield.discordbot.tickets.panels.TerritoryReg
import net.dv8tion.jda.api.entities.Guild


fun modalSubmitListener(jda: JDA, guild: Guild) {

    jda.listener<ModalInteractionEvent> { event ->

        when (event.modalId) {

            "Court_Panel" -> {
                Court.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "ModerComplaint_panel" -> {
                ModerComplaint.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Parliament_panel" -> {
                ParliamentQuestion.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Patent_panel" -> {
                Patent.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "TechnicalQuestion_panel" -> {
                TechnicalQuestion.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Territory_panel" -> {
                TerritoryReg.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener

            }

            "modal-user-action" -> {

                VerdictModalHandler.onModalHandler(event, guild)
                event.deferEdit().queue()
                return@listener
            }

            "verdict-modal"  -> {

                VerdictModalHandler.onModalHandler(event, guild)
                event.deferEdit().queue()
                return@listener
            }

        }
    }
}









