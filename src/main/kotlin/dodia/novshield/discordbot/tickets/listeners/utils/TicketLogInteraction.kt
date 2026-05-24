package dodia.novshield.discordbot.tickets.listeners.utils

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import org.jetbrains.exposed.sql.transactions.transaction

import dodia.novshield.discordbot.tickets.panels.Patent
import dodia.novshield.discordbot.tickets.panels.TerritoryReg
import dodia.novshield.discordbot.tickets.panels.TechnicalQuestion
import dodia.novshield.discordbot.tickets.panels.Court
import dodia.novshield.discordbot.tickets.panels.ParliamentQuestion
import dodia.novshield.discordbot.tickets.panels.ModerComplaint

import dodia.novshield.discordbot.tickets.database.Ticket
import dodia.novshield.discordbot.tickets.database.Tickets

fun ticketLogInteraction(event: ButtonInteractionEvent) {
    val channelId = event.channel.idLong
    var logSent = false


    transaction {

        val dbTicket = Ticket.find { Tickets.channelId eq channelId }.firstOrNull()

        if (dbTicket != null) {

            when (dbTicket.ticketType) {
                "модер-жалоба" -> ModerComplaint.sendTicketLog(event, dbTicket)
                "тех-вопрос"   -> TechnicalQuestion.sendTicketLog(event, dbTicket)
                "территория"   -> TerritoryReg.sendTicketLog(event, dbTicket)
                "суд"          -> Court.sendTicketLog(event, dbTicket)
                "парламент"    -> ParliamentQuestion.sendTicketLog(event, dbTicket)
                "патент"       -> Patent.sendTicketLog(event, dbTicket)
                else -> {
                    println("Неизвестный тип тикета в БД: ${dbTicket.ticketType}")
                }
            }
            logSent = true
        } else {
            println("Тикет для канала $channelId не найден в базе данных.")
        }
    }

    if (logSent) {
        event.reply("Лог тикета успешно отправлен в архивный канал ✅").setEphemeral(true).queue()
    } else {
        event.reply("❌ Не удалось отправить лог: тикет не найден в базе данных.").setEphemeral(true).queue()
    }
}