package ca.bc.gov.health.qa.autotest.core.util.capture;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import ca.bc.gov.health.qa.autotest.core.util.event.BasicNamedEvent;
import ca.bc.gov.health.qa.autotest.core.util.io.FileIOUtils;

/**
 * TODO (AZ) - doc
 */
public class CaptureEvent
extends BasicNamedEvent
{
    private static final String ARTIFACT_TIME_PATTERN = "HH.mm.ss.SSS";
    private static final String EVENT_TIME_PATTERN    = "yyyy.MM.dd-HH.mm.ss.SSS";

    private final List<Artifact> sequentialArtifactList_;
    private final List<Artifact> summaryArtifactList_;
    private final Path           path_;

    /**
     * Creates a new capture event.
     *
     * @param name
     *        the name of the capture event
     *
     * @param time
     *        the time of the capture event
     *
     * @param path
     *        the path of the capture event
     *
     * @throws NullPointerException
     *         if {@code path} is {@code null}
     */
    private CaptureEvent(String name, Instant time, Path path)
    {
        super(name, time);
        requireNonNull(path, "Null capture event path.");
        sequentialArtifactList_ = new ArrayList<>();
        summaryArtifactList_    = new ArrayList<>();
        path_                   = path;
    }

    /**
     * Adds a sequential artifact to this capture event.
     *
     * @param artifact
     *        the artifact to add
     *
     * @throws NullPointerException
     *         if the artifact is {@code null}
     */
    public void addSequentialArtifact(Artifact artifact)
    {
        requireNonNull(artifact, "Null artifact.");
        sequentialArtifactList_.add(artifact);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param artifactCollection
     *        ???
     *
     * @throws NullPointerException
     *         if the artifactCollection is {@code null} or it contains a {@code null} artifact
     */
    public void addSequentialArtifacts(Collection<? extends Artifact> artifactCollection)
    {
        for (Artifact artifact : artifactCollection)
        {
            addSequentialArtifact(artifact);
        }
    }

    /**
     * Adds a summary artifact to this capture event.
     *
     * @param artifact
     *        the artifact to add
     *
     * @throws NullPointerException
     *         if the artifact is {@code null}
     */
    public void addSummaryArtifact(Artifact artifact)
    {
        requireNonNull(artifact, "Null artifact.");
        summaryArtifactList_.add(artifact);
    }

    /**
     * Creates a new capture event.
     *
     * @param parentPath
     *        the parent path of the capture event
     *
     * @param name
     *        the name of the capture event
     *
     * @param time
     *        the time of the event,
     *        or {@code null} to indicate the current time
     *
     * @return the new capture event
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static CaptureEvent createCaptureEvent(Path parentPath, String name, Instant time)
    throws IOException
    {
        requireNonNull(name, "Null capture event name.");
        if (time == null)
        {
            time = Instant.now();
        }
        String timestamp = DateTimeFormatter
                .ofPattern(EVENT_TIME_PATTERN)
                .withZone(ZoneId.systemDefault())
                .format(time);
        Files.createDirectories(parentPath);
        Path path = FileIOUtils.createSequentialDirectory(
                parentPath, FileIOUtils.sanitizeFileName(timestamp + "-" + name));
        return new CaptureEvent(name, time, path);
    }

    /**
     * Returns the path of the capture event.
     *
     * @return the path of the capture event
     */
    public Path getPath()
    {
        return path_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public void renderArtifacts()
    throws IOException
    {
        renderArtifacts(sequentialArtifactList_, true);
        renderArtifacts(summaryArtifactList_, false);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param artifactCollection
     *        ???
     *
     * @param sequential
     *        ???
     *
     * @throws IllegalArgumentException
     *         if a duplicate artifact path is detected
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    private void renderArtifacts(
            Collection<? extends Artifact> artifactCollection, boolean sequential)
    throws IOException
    {
        Set<Path> pathSet = new HashSet<>();
        int sequenceNumber = 1;
        for (Artifact artifact : artifactCollection)
        {
            String name  = artifact.getName();
            byte[] bytes = artifact.render();
            if (sequential)
            {
                Instant time = artifact.getTime();
                String timestamp = DateTimeFormatter
                        .ofPattern(ARTIFACT_TIME_PATTERN)
                        .withZone(ZoneId.systemDefault())
                        .format(time);
                name = String.format("%02d-%s-%s", sequenceNumber, timestamp, name);
                sequenceNumber++;
            }
            Path path = FileIOUtils.createSanitizedPath(getPath(), name);
            if (!pathSet.add(path))
            {
                String msg = String.format("Duplicate artifact path (%s).", path);
                throw new IllegalArgumentException(msg);
            }
            FileUtils.writeByteArrayToFile(path.toFile(), bytes);
        }
    }
}
