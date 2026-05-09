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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.valentinerutto.rickandmortynative.CharacterDetailUiState
import com.valentinerutto.rickandmortynative.CharacterViewmodel
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.data.network.models.Episode
import com.valentinerutto.rickandmortynative.ui.theme.PortalBackground
import com.valentinerutto.rickandmortynative.ui.theme.PortalBorder
import com.valentinerutto.rickandmortynative.ui.theme.PortalGreen
import com.valentinerutto.rickandmortynative.ui.theme.PortalGreenDark
import com.valentinerutto.rickandmortynative.ui.theme.PortalMuted
import com.valentinerutto.rickandmortynative.ui.theme.PortalSurface
import com.valentinerutto.rickandmortynative.ui.theme.PortalSurfaceHigh
import com.valentinerutto.rickandmortynative.ui.theme.PortalText


@Composable
fun CharacterDetailScreen(
    viewModel: CharacterViewmodel,
    characterId: Int,
    onBackClick: () -> Unit
) {
    val detailFlow = remember(characterId) { viewModel.characterDetailState(characterId) }
    val state by detailFlow.collectAsStateWithLifecycle(
        initialValue = CharacterDetailUiState()
    )

    CharacterDetailContent(
        character = state.character,
        episodeIds = state.episodeIds,
        episodes = state.episodes,
        isLoadingEpisodes = state.isLoadingEpisodes,
        episodeError = state.episodeError,
        onBackClick = onBackClick
    )
}

@Composable
private fun CharacterDetailContent(
    character: CharacterEntity?,
    episodeIds: List<Int>,
    episodes: List<Episode>,
    isLoadingEpisodes: Boolean,
    episodeError: String?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PortalBackground)
            .verticalScroll(rememberScrollState())
    ) {
        DetailTopBar(onBackClick = onBackClick)

        if (character == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PortalGreen)
            }
            return@Column
        }

        Box {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.92f)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                PortalBackground.copy(alpha = 0.25f),
                                PortalBackground
                            )
                        )
                    )
            )
        }

        DossierCard(
            character = character,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 34.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "First 3 Appearances",
                color = PortalGreen,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "VIEW LOG",
                color = PortalText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EpisodeSection(
            episodeIds = episodeIds,
            episodes = episodes,
            isLoadingEpisodes = isLoadingEpisodes,
            episodeError = episodeError
        )

    }
}

@Composable
private fun DetailTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(Color(0xFF09131E))
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = PortalGreen
            )
        }
        Text(
            text = "SUBJECT DOSSIER",
            color = PortalGreen,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

    }
}

@Composable
private fun DossierCard(character: CharacterEntity, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PortalSurface.copy(alpha = 0.96f)),
        border = BorderStroke(1.dp, PortalBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = character.name,
                        color = PortalGreen,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SmallBadge(character.status)
                        SmallBadge("C-${character.id}")
                    }
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                DetailField("SPECIES", character.species, Modifier.weight(1f))
                DetailField("GENDER", character.gender, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            IconField(Icons.Default.Public, "ORIGIN", character.origin)
            Spacer(modifier = Modifier.height(18.dp))
            IconField(Icons.Default.LocationOn, "LAST KNOWN LOCATION", character.location)
        }
    }
}

@Composable
private fun SmallBadge(text: String) {
    Text(
        text = text.uppercase(),
        color = PortalGreen,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(PortalGreenDark)
            .border(1.dp, PortalGreen.copy(alpha = 0.55f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
private fun DetailField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = PortalMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            color = PortalText,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun IconField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Text(
        text = label,
        color = PortalMuted,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PortalGreen,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            color = PortalText,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
private fun EpisodeSection(
    episodeIds: List<Int>,
    episodes: List<Episode>,
    isLoadingEpisodes: Boolean,
    episodeError: String?
) {
    when {
        episodeIds.isEmpty() -> {
            Text(
                text = "No episode for this subject.",
                color = PortalMuted,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }


        isLoadingEpisodes -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PortalGreen,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        episodeError != null -> {
            Text(
                text = episodeError,
                color = PortalMuted,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }

        else -> {
            episodes.forEachIndexed { index, episode ->
                EpisodeRow(
                    number = index + 1,
                    episode = episode,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}
@Composable
private fun EpisodeRow(number: Int, episode: Episode, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PortalSurface)
            .border(1.dp, PortalBorder, RoundedCornerShape(8.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(if (number == 2) Color(0xFF3B4C59) else PortalGreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString().padStart(2, '0'),
                color = if (number == 2) PortalText else PortalGreen,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = episode.name,
                color = PortalText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = episode.airDate,
                color = PortalMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = episode.episode,
            color = PortalGreen,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = PortalMuted
        )
    }
}

@Composable
private fun MetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PortalSurface),
        border = BorderStroke(1.dp, PortalBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(170.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PortalGreen
            )
            Column {
                Text(
                    text = label,
                    color = PortalMuted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    color = valueColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}