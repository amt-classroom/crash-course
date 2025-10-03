package ch.heigvd.amt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.StringJoiner;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Probe probe;

    @NotNull
    private Instant timestamp;

    @NotNull
    private Boolean up;

    @NotNull
    private Integer duration;

    public Status() {
    }

    public Status(Probe probe, Instant timestamp, Boolean up, Integer duration) {
        this.probe = probe;
        this.timestamp = timestamp;
        this.up = up;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Probe getProbe() {
        return probe;
    }

    public void setProbe(Probe probe) {
        this.probe = probe;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Status.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("probe=" + probe)
                .add("timestamp=" + timestamp)
                .add("up=" + up)
                .add("duration=" + duration)
                .toString();
    }
}
