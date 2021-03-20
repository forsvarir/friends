package nl.jovmit.friends.signup

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialsValidationTest {

  @ParameterizedTest
  @CsvSource(
    "'email'",
    "'a@b.c'",
    "'ab@b.c'",
    "'ab@bc.c'",
    "''",
    "'     '",
  )
  fun invalidEmail(email: String) {
    val viewModel = SignUpViewModel()

    viewModel.createAccount(email, ":password:", ":about:")

    assertEquals(SignUpState.BadEmail, viewModel.signUpState.value)
  }
}