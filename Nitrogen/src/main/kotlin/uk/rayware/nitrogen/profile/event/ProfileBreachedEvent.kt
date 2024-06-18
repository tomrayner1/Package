package uk.rayware.nitrogen.profile.event

import uk.rayware.nitrogen.profile.Profile
import uk.rayware.nitrogen.util.NitrogenEvent

class ProfileBreachedEvent(val profile: Profile, val code: Int) : NitrogenEvent()