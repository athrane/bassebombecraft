# Map Template — Wayfinding

Copy to `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` (next available `<n>`) and fill in the placeholders. Replace each `<...>` before saving.

```markdown
# Wayfinding Map <n>: <short human title>

## Destination

<1–2 sentences describing what "done" looks like for this whole effort.>

## Notes

<Domain context and standing preferences every ticket should inherit. Examples: "Java 17 + Forge 1.17.1 — no legacy OpenGL patterns," "AI-side selection lands before GUI," "match the existing `[nnn]-[slug].md` PR naming." Keep this short — a handful of bullets max.>

## Decisions So Far

| Ticket | Gist | Resolved |
|--------|------|----------|
| _none yet — populated as tickets resolve_ | | |

## Not Yet Specified

- _fog too unclear to phrase as a ticket question yet; promote to a ticket the moment it becomes specifiable_

## Out of Scope

- _work consciously ruled out, with a one-line reason each_

## Tickets

| Ticket | Type | Status | Blocked By | File |
|--------|------|--------|------------|------|
| <slug-1> | research \| grilling \| prototype \| task | open | — | [tickets/<slug-1>.md](./wayfinding-<n>-tickets/<slug-1>.md) |
| <slug-2> | research \| grilling \| prototype \| task | open | <slug-1> | [tickets/<slug-2>.md](./wayfinding-<n>-tickets/<slug-2>.md) |
```

## Usage Notes

- `<n>` is the next available number, sequential within this wayfinding effort (not random like the PR spec numbering).
- `<slug>` is short, lowercase, hyphen-separated; mirrors the PR file naming convention.
- The Tickets table is the live index — keep it in sync with the actual ticket files.
- Resolved tickets leave the Tickets table and gain a one-line gist row in Decisions So Far.