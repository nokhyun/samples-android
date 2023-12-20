package com.nokhyun.uiexam.immutableExam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nokhyun.uiexam.logger

@Immutable
data class Person(val name: String, val phoneNumber: String)

@Composable
fun PersonView(person: Person) {
    Column {
        Row {
            Text("Name: ")
            Text(person.name)
        }

        Row {
            Text("Phone: ")
            Text(person.phoneNumber)
        }
    }
}

@Composable
fun PeopleView(people: List<Person>) {
    Column {
        for (person in people) {
            PersonView(person = person)
        }
    }
}

@Composable
fun StableTestScreen() {
    var selected by remember { mutableStateOf(false) }
    Column {
        Checkbox(
            checked = selected,
            onCheckedChange = { selected = it }
        )
        ContactsList(
            ContactListState(
                isLoading = selected,
                names = listOf("Kim")
            )
        )
    }
}

@Composable
fun ContactsList(
    state: ContactListState
) {
    logger { "[ContactsList]" }
    Box(
        modifier = Modifier
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = state.names.toString())
        }
    }
}

@Stable
data class ContactListState(
    val isLoading: Boolean,
    val names: List<String>,
)

@Preview
@Composable
fun StableTestScreenPreview() {
    StableTestScreen()
}