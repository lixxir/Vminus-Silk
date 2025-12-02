package net.lixir.vminus.component

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent

interface CapeComponent : AutoSyncedComponent {
    var capeName: String
}
