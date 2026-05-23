package dodia.novshield.discordbot.tickets.listeners


import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.messages.MessageEditData
import dev.minn.jda.ktx.events.onStringSelect


import dodia.novshield.discordbot.tickets.panels.Court
import dodia.novshield.discordbot.tickets.panels.ModerComplaint
import dodia.novshield.discordbot.tickets.panels.ParliamentQuestion
import dodia.novshield.discordbot.tickets.panels.Patent
import dodia.novshield.discordbot.tickets.panels.TechnicalQuestion
import dodia.novshield.discordbot.tickets.panels.TerritoryReg

fun selectMenuListener(jda: JDA) {

    jda.onStringSelect("sel_menu") { event ->
        val selected = event.selectedOptions.firstOrNull()?.value ?: "Нет данных"

        val editData = MessageEditData.fromMessage(event.message)

        when (selected) {
            "SelectMenu_Court" -> {
                Court.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }
            "SelectMenu_ModerComplaint" -> {
                ModerComplaint.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }
            "SelectMenu_ParliamentQuestion" -> {
                ParliamentQuestion.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }

            "SelectMenu_Patent" -> {
                Patent.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }
            "SelectMenu_TechnicalQuestion" -> {
                TechnicalQuestion.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }
            "SelectMenu_LandRegistration" -> {
                TerritoryReg.showModal(event)
                event.message.editMessage(editData).queue()
                return@onStringSelect
            }

            else -> event.hook.sendMessage("Вы выбрали: $selected")
                .setEphemeral(true)
                .queue()




        }

        event.deferEdit().queue()

    }
}