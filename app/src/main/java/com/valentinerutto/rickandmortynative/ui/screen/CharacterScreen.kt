package com.valentinerutto.rickandmortynative.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.valentinerutto.rickandmortynative.CharacterViewmodel
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.ui.theme.PortalBorder
import com.valentinerutto.rickandmortynative.ui.theme.PortalDanger
import com.valentinerutto.rickandmortynative.ui.theme.PortalGreen
import com.valentinerutto.rickandmortynative.ui.theme.PortalMuted
import com.valentinerutto.rickandmortynative.ui.theme.PortalSurface
import com.valentinerutto.rickandmortynative.ui.theme.PortalSurfaceHigh
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
                    CharacterCard(character)
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
private fun CharacterCard(
    character: CharacterEntity
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PortalSurface),
        border = BorderStroke(1.dp, PortalBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.28f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(PortalSurfaceHigh)
            )
            StatusBadge(
                status = character.status,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, top = 10.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    color = PortalText,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Species: ${character.species}",
                    color = PortalText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = PortalMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "  ${character.location}",
                        color = PortalMuted,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

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
private fun FilterRow(
    label: String,
    options: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = PortalGreen,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.72f)
        )
        options.forEach { option -> FilterChip(
                text = option,
                selected = selected.equals(option, ignoreCase = true),
                onClick = {
                    onSelected(if (selected.equals(option, ignoreCase = true)) null else option)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall
            )
        },
        border = BorderStroke(1.dp, if (selected) PortalGreen else PortalBorder),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) PortalGreen else Color.Transparent,
            labelColor = if (selected) Color(0xFF152100) else PortalText
        ),
        shape = RoundedCornerShape(50),
        modifier = modifier.height(32.dp)
    )
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

        else -> "Could not reach the portal network. Showing cached data."}}


