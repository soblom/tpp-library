package de.soerenblom.app.domain.model.entities

// Libraries has members (registered users)
data class Member(val name: MemberName, val id: MemberId)
typealias MembershipList = Collection<Member>

typealias MemberName = String
typealias MemberId = Int

