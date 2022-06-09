package com.mukatalab.scopeActions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun notifyError(project: Project?, msg: String) {
    NotificationGroupManager.getInstance().getNotificationGroup("Scope Actions Notification")
        .createNotification(msg, NotificationType.ERROR).notify(project)
}

fun notifyPotentiallyMissingSdk(project: Project) {
    notifyError(
        project,
        "Failed to find element. You may need to install language SDK for code introspection to work."
    )
}