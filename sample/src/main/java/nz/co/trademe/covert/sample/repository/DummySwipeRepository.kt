package nz.co.trademe.covert.sample.repository

/**
 * Class which acts in place of something which would perform a swipe action
 */
class DummySwipeRepository {

    private val activeStates: MutableMap<Int, Boolean> = hashMapOf()

    fun toggleActiveState(index: Int) {
         activeStates[index] = !(activeStates[index] ?: false)
    }

    fun isActive(index: Int): Boolean = activeStates[index] ?: false
}