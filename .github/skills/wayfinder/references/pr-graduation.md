# PR Graduation — Wayfinding

A wayfinder map never produces code. When enough tickets are resolved that a concrete, implementable slice exists, **graduate** the slice into a real PR spec file under `.github/.requirements/pr/`.

## When to Graduate

A slice is ready for graduation when:

1. The destination described in its tickets is unambiguous (you could hand it to another developer and they could implement it without further questions).
2. Every decision the slice depends on is either resolved in the map's Decisions So Far or lives in a resolved ticket.
4. The slice fits in **one PR** — if it doesn't, graduate multiple slices, one PR each.

If any of these aren't true, more tickets need to resolve first. Don't graduate prematurely.

## Procedure

1. **Pick the next issue number.** Look at existing files in `.github/.requirements/pr/` to find the largest `nnnn` currently used; pick the next one (or use a known next GitHub issue number if one was filed during charting).
2. **Slug the file.** Mirror the PR naming convention: `[nnnn]-[short-kebab-summary].md`. The slug should be the user-visible summary of the PR — not "graduate-from-wayfinding-3".
3. **Invoke [`00-00-create-pr`](../../00-00-create-pr/SKILL.md) with a problem description that summarises the resolved tickets.** The skill is the canonical tool for producing the PR spec file — it fills in the bundled template, runs the Java/Gradle/Forge conventions check, and writes the file to the correct location. Supply it with:
   - The user-visible outcome (one or two sentences)
   - The list of resolved ticket files this slice consolidates (so the skill can link them under **Related Issues**)
   - The GitHub issue number picked in step 1
   - Any standing constraints from the map's **Notes** section (e.g. "Java 17 only," "PoseStack-based rendering")

   The skill will write `.github/.requirements/pr/[nnnn]-[slug].md` populated from [its bundled template](../../00-00-create-pr/templates/pr-template.md). The produced file's **Related Issues** should link the source ticket files so the map → spec traceability is preserved.
4. **Update the map**:
   - In the Tickets table, mark the resolved tickets that this PR spec consolidates with a footnote pointing at the new PR file.
   - Add a one-line gist to Decisions So Far: "PR #<nnnn> graduated: <slug>".
   - If graduating consumed all open tickets, the map is complete — note that in Destination as done, or move the map to an `archive/` subfolder.

## Anti-Patterns

- **Don't write a PR spec file by hand** — invoke [`00-00-create-pr`](../../00-00-create-pr/SKILL.md) instead. The skill exists exactly so PR specs are consistent and follow the project's Java/Gradle/Forge conventions.
- **Don't write a PR spec file from a map ticket's Resolution text verbatim** — the PR spec is a fresh document shaped for an implementer, not a copy-paste of planning notes.
- **Don't bypass the bundled template** — every PR spec produced by this repo's `00-00-create-pr` skill follows [its template](../../00-00-create-pr/templates/pr-template.md). Consistency matters.
- **Don't graduate a slice that still has open, related tickets** — graduate later when those tickets resolve, or split the slice.
- **Don't link the PR file from the ticket's Resolution** as a replacement for the map update — both updates are required (the ticket Resolution records the decision; the map gists the graduation).

## Worked Micro-Example

Suppose the map's Tickets table contains three resolved tickets — `potions-catalog-shape`, `effects-duration-rule`, `effects-stack-policy` — all in service of one slice: "introduce a typed potion-effect registry."

After graduation:

- A new file `.github/.requirements/pr/1426-potion-effect-registry.md` exists, populated from the PR template, with `Related Issues` listing links back to the three ticket files.
- The map's Tickets table is empty (or contains only unrelated, unresolved tickets).
- Decisions So Far gains: `| (combined) | PR #1426 graduated: potion-effect-registry | 2026-09-07 |`.

That's graduation. No code changed — the implementer picks up the PR spec file and runs the normal PR workflow.