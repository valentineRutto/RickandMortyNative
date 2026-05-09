package com.valentinerutto.rickandmortynative.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.valentinerutto.rickandmortynative.CharacterViewmodel
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.ui.theme.PortalDanger
import com.valentinerutto.rickandmortynative.ui.theme.PortalGreen
import com.valentinerutto.rickandmortynative.ui.theme.PortalMuted
import com.valentinerutto.rickandmortynative.ui.theme.PortalText
import retrofit2.HttpException


@Composable
    fun CharacterScreen(
        viewModel: CharacterViewmodel
    ) {
        val characters = viewModel.characters.collectAsLazyPagingItems()

        LazyColumn {
            items(characters.itemCount) { index ->
                val character = characters[index]
                if (character != null) {
                    CharacterItem(character)
                }
            }

            characters.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = loadState.refresh as LoadState.Error
                        item {
                            Text(text = error.error.message ?: "Something went wrong")
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = loadState.append as LoadState.Error
                        item {
                            Text(text = error.error.message ?: "Could not load more")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CharacterItem(character: CharacterEntity) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = character.name)
                Text(text = "${character.species} - ${character.status}")
                Text(text = character.location)
            }
        }
    }
@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}, modifier = Modifier.size(34.dp)) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = PortalGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = "Portal Explorer",
            color = PortalGreen,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )
        IconButton(onClick = {}, modifier = Modifier.size(34.dp)) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = PortalText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
@Composable
private fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val color = when (status.lowercase()) {
        "alive" -> PortalGreen
        "dead" -> PortalDanger
        else -> PortalMuted
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.7f))
            .border(1.dp, color, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Text(
            text = "  ${status.uppercase()}",
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        color = PortalMuted,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(top = 24.dp)
    )
}

private fun LoadState.errorMessage(): String? {
    val throwable = (this as? LoadState.Error)?.error ?: return null
    return when (throwable) {
        is HttpException -> if (throwable.code() == 404) {
            "No characters found in this dimension."
        } else {
            "The portal API returned ${throwable.code()}."
        }

        else -> "Could not reach the portal network. Showing cached data."


