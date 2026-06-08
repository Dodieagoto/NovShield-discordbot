package dodia.novshield.discordbot.tickets.commands

import dev.minn.jda.ktx.interactions.commands.*
import net.dv8tion.jda.api.JDA

object CommandRegister {

    fun registerCommands(jda: JDA) {

        jda.updateCommands {
            slash("управление_участниками", "Управление участниками тикета"){
                restrict(
                    guild = true,
                )
            }
            slash("вердикт","Вынести вердикт по тикету"){
                restrict(
                    guild = true,
                )
            }
        }.queue()

        CommandsHandler.onCommand(jda)

    }
}