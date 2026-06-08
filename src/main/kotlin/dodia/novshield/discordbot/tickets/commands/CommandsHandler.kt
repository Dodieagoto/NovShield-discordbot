package dodia.novshield.discordbot.tickets.commands

import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import dodia.novshield.discordbot.tickets.commands.membercontrol.MemberControlModal

object CommandsHandler {

    fun onCommand(jda: JDA) {

        jda.onCommand("управление_участниками") { event ->

            MemberControlModal.showModal(event)

        }

    }

}