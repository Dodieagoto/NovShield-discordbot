package dodia.novshield.discordbot.tickets.commands

import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import dodia.novshield.discordbot.tickets.commands.membercontrol.MemberControlModal
import dodia.novshield.discordbot.tickets.commands.verdict.VerdictModal

object CommandsHandler {

    fun onCommand(jda: JDA) {

        jda.onCommand("управление_участниками") { event ->

            MemberControlModal.showModal(event)

        }

        jda.onCommand("вердикт") { event ->

            VerdictModal.showModal(event)

        }

    }

}