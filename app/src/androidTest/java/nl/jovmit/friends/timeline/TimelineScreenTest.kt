package nl.jovmit.friends.timeline

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.domain.post.*
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.UserCatalog
import org.junit.*
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class TimelineScreenTest {

  @get:Rule
  val timelineTestRule = createAndroidComposeRule<MainActivity>()

  val defaultPostCatalog: PostCatalog = GlobalContext.get().get()
  val defaultUserCatalog: UserCatalog = GlobalContext.get().get()

  @Test
  fun showsEmptyTimelineMessage() {
    val email = "lucy@friends.com"
    val password = "passPASS123#@"
    launchTimelineFor(email, password, timelineTestRule) {
      //no operation
    } verify {
      emptyTimelineMessageIsDisplayed()
    }
  }

  @Test
  fun showsAvailablePosts() {
    val email = "bob@friends.com"
    val password = "b0bPaS#2021"
    val post1 = Post("post1", "bobId", "This is Bob's first post", 1L)
    val post2 = Post("post2", "bobId", "Bob's second post is here!", 2L)
    replacePostCatalogWith(InMemoryPostCatalog(mutableListOf(post1, post2)))

    launchTimelineFor(email, password, timelineTestRule) {
      //no operation
    } verify {
      postsAreDisplayed(post1, post2)
    }
  }

  @Test
  fun opensPostComposer() {
    launchTimelineFor("test@email.com", "sOmEPa$123", timelineTestRule) {
      tapOnCreateNewPost()
    } verify {
      newPostComposerIsDisplayed()
    }
  }

  @Test
  fun showsLoadingIndicator() {
    replacePostCatalogWith(DelayingPostsCatalog())
    launchTimelineFor("testLoading@email.com", "sOmEPa$123", timelineTestRule) {
      //no operation
    } verify {
      loadingIndicatorIsDisplayed()
    }
  }

  @Test
  fun showsBackendError() {
    replacePostCatalogWith(UnavailablePostCatalog())
    launchTimelineFor("backendError@friends.com", "sOmEPa$123", timelineTestRule) {
      //no operation
    } verify {
      backendErrorIsDisplayed()
    }
  }

  @Test
  fun showsOfflineError() {
    replacePostCatalogWith(OfflinePostCatalog())
    launchTimelineFor("offlineError@friends.com", "sOmEPa$123", timelineTestRule) {
      //no operation
    } verify {
      offlineErrorIsDisplayed()
    }
  }

  @Before
  fun before() {
    replaceUserCatalogWith(InMemoryUserCatalog())
  }

  @After
  fun tearDown() {
    replacePostCatalogWith(defaultPostCatalog)
    replaceUserCatalogWith(defaultUserCatalog)
  }


  private fun replacePostCatalogWith(postsCatalog: PostCatalog) {
    val replaceModule = module {
      factory { postsCatalog }
    }
    loadKoinModules(replaceModule)
  }

  private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
    val replaceModule = module {
      factory { userCatalog }
    }
    loadKoinModules(replaceModule)
  }
}
