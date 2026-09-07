# Ticket Template — Wayfinding

Copy to `.github/.requirements/wayfinding/wayfinding-<n>-tickets/<ticket-slug>.md` and fill in the placeholders.

```markdown
# Ticket: <one-line title>

| Field | Value |
|-------|-------|
| Wayfinding Map | [<n>-<map-slug>](../wayfinding-<n>-<map-slug>.md) |
| Type | research \| grilling \| prototype \| task |
| Status | open \| claimed \| resolved \| ruled-out |
| Blocked By | _comma-separated links, or "—" if none_ |
| GitHub Issue | _tracking issue #, or "—" if none_ |

## Question

<The one thing this ticket must answer. Single sentence if possible. If you can't phrase it as one sentence, the ticket is too big — split it.>

## Resolution

_Filled in once Status moves to `resolved` or `ruled-out`. Cite the source of the decision (Explore agent output, grilling answers, prototype artifact path, task completion evidence)._
```

## Field Guidance

| Field | How to fill |
|-------|-------------|
| `Type` | `research` = explore codebase/external docs, no user input; `grilling` = ask the user with `ask_questions`; `prototype` = write a throwaway spike; `task` = concrete action the user must perform |
| `Status` | Start at `open`. Set to `claimed` before starting work in a session. Set to `resolved` after recording Resolution. Set to `ruled-out` if the work is discarded (move the ticket under Out of Scope in the map, link here) |
| `Blocked By` | Use relative links: `[<slug>](./<slug>.md)`. Multiple blockers comma-separated. A ticket's Question must be unanswerable until all Blocked By tickets are resolved |
| `Question` | The single decision this ticket exists to make. If you find yourself writing "and also," split into another ticket |

## Lifecycle

1. **Created** with `Status: open`.
2. **Claimed** (`Status: claimed`) at the start of the session that will resolve it — prevents another session from picking up the same work.
3. **Resolved** (`Status: resolved`) — record the decision under Resolution, then update the parent map: remove from the open Tickets table, add a one-line gist row to Decisions So Far.
4. **Ruled out** (`Status: ruled-out`) — record why under Resolution, then move under Out of Scope in the parent map with a one-line reason.