---
name: 00-02-grill-me
description: >
  Option-driven design interview to stress-test a plan before implementation: one blocking
  question, concrete options, recommended default. Use when grilling a plan or design.
---

# Grill-Me Skill — Option-Driven Design Interview

This skill governs the structured interview workflow used to stress-test a plan or design before implementation. It replaces open-ended dialogue with a one-question-at-a-time interview where each question surfaces the next blocking decision, presents concrete options, marks the recommended default, and records the outcome before moving on.

## When to Use This Skill

Load this skill when:

- The user says "grill me on this plan", "challenge my design", "help me think through this", or similar
- The user has a plan or design they want probed before implementation begins
- The user asks you to surface weaknesses, probe assumptions, or stress-test an approach
- A planning session benefits from structured decision capture rather than free-form discussion

> **Scope**: This skill governs interview _flow_ only — not domain knowledge. Domain knowledge (Forge registration, event buses, the `Operator2`/`Ports` pattern, client/server split, Forge API migrations) comes from the codebase and the [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) skill. Typical usage: read the PR requirements file or plan to understand the task scope, then invoke `00-02-grill-me` to probe it for weaknesses before starting implementation.
>
> **Integration with the development loop**: `00-02-grill-me` is invoked from within the other `00-*` workflow skills, not just on direct user request:
> - [`00-05-wayfinder`](../00-05-wayfinder/SKILL.md) — invoked to resolve `grilling`-type tickets on a wayfinding map; if a grill-me session keeps surfacing new blocking dependencies instead of converging, that's the signal to chart a map with `00-05-wayfinder` instead of continuing the interview
> - [`00-00-create-pr`](../00-00-create-pr/SKILL.md) — before finalizing a PR requirements file whose Implementation Plan involves significant or ambiguous design decisions
> - [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md) — before beginning execution, when the file/pattern plan involves significant architectural decisions or ambiguity

---

## Section 1 — Inspection-First Rule

**Before asking the user anything**, search code, docs, and open files for relevant context.

- If the answer to a potential question is already derivable from context (existing implementation, docs, prior decisions in the conversation), record it in the recap and **skip the question**.
- Only ask about decisions that are genuinely unresolved after inspection.
- State what you found during inspection at the start of the session: "I inspected X and Y. The following decisions are already resolved: …"

This prevents asking the user to re-explain things that are already documented.

---

## Section 1.5 — Contradiction Scan

**Before asking Decision 1**, scan the plan or design for pairs of requirements that are mutually exclusive or directly contradictory.

- Produce an explicit bulleted list titled **"Contradictions found:"** with one line per conflict, naming the two conflicting requirements and the nature of the conflict.
- If no contradictions exist, write **"No contradictions detected."** and proceed to Section 2.
- Contradictions are treated the same as unresolved decisions: each one is recorded in the recap (Section 9) as a `⚠ Contradiction:` entry when resolved.

---

## Section 2 — Blocking-Question Identification

Select the **next question** using this heuristic:

> Pick the unresolved decision that, if answered, **unblocks the most downstream decisions**.

**Contradiction-resolution questions have highest priority.** If the Contradiction Scan (Section 1.5) found any conflicts, ask the user to resolve each contradiction before asking any standard design decision. A contradiction question follows the same selectable-UI rules (Section 3) and must be recorded in the recap before the first standard blocking question.

Concretely (for standard design decisions, after all contradictions are resolved):
1. List all unresolved decisions visible from the plan.
2. Draw an informal dependency graph: which decisions constrain which others?
3. Ask the decision at the root of the longest dependency chain first.
4. If two decisions are independent and equally blocking, prefer the one with higher risk if chosen incorrectly.

Ask **exactly one question per response**. Do not bundle multiple questions.

---

## Section 3 — Selectable-UI Preference

Use `AskUserQuestion` for every interview question. If it is not available, use the plain-markdown fallback (Section 4).

Requirements for each call:

| Field | `AskUserQuestion` |
|-------|-------------------|
| Question text | State the decision being made, not a yes/no question; end with `?`; keep it ≤ 200 characters — rephrase to fit |
| Options | 2–4 concrete alternatives (see Section 5); the tool adds "Other" for free-form input automatically |
| Recommended default | Put the recommended option **first** and end its label with `(Recommended)` |
| Header | `header` ≤ 12 characters, a short topic label (e.g. `Render stage`); put the `Decision N —` numbering in the question text instead |
| Non-recommended options | Each `description` should begin with "Deviation from recommended default." followed by what it means |

If a decision genuinely has more than 4 viable options, group them into at most 4 materially different directions and settle the sub-choice in the next question.

After the selectable question, add the recommended-option rationale (Section 6) as prose below the tool call.

---

## Section 4 — Plain-Markdown Fallback

When no selectable-question tool is available, render the question as:

```
**Decision N — [Topic]**

[Decision statement]

1. [Option A]
2. ⭐ [Option B] _(recommended)_
3. [Option C]

_Or describe your own approach._
```

Rules:
- Mark exactly one option with ⭐ and `_(recommended)_`.
- Include the explicit sentence: "Or describe your own approach." to invite custom input.
- Follow with the recommended-option rationale (Section 6).

---

## Section 5 — Option-Quality Criteria

Each option presented in a question must satisfy all five criteria:

1. **Concrete and actionable** — The option names a specific approach, not a vague direction (e.g. "Filter on `RenderLevelStageEvent.Stage.AFTER_PARTICLES` in every world-space renderer" not "Pick a render stage").
2. **Materially different in consequence** — Choosing it must lead to observably different implementation decisions downstream. Do not present options that are effectively synonyms.
3. **Decision-shaping** — Choosing it changes at least one later decision in the interview. Options that have no downstream effect are not blocking and should not be surfaced.
4. **Same level of abstraction** — Do not mix architectural-level options with implementation-detail options in the same question.
5. **Free of obvious dominance** — No option should be trivially better on all dimensions. If one option dominates, merge it with the recommended option and remove the dominated one.

---

## Section 6 — Recommended-Option Rationale

After each question (in both UI modes), provide exactly **1–2 sentences** explaining:

1. Why the recommended default is the best starting point for this plan.
2. Its key assumption and the main tradeoff accepted by choosing it.

Format:

> **Why ⭐ [Recommended Option]**: [1–2 sentences.] Key assumption: [assumption]. Main tradeoff: [tradeoff].

---

## Section 7 — Branching Behaviour Table

| User's answer | Record in recap | Note deviation? | Flag risk? |
|--------------|-----------------|-----------------|------------|
| Recommended option chosen | `Decision N — [topic]: [option] (recommended)` | No | No |
| Non-recommended option chosen | `Decision N — [topic]: [option] (deviation)` | Yes — one sentence why this deviates | Yes — name the risk introduced |
| Custom / freeform answer | `Decision N — [topic]: [summary of custom answer] (custom)` | Yes — one sentence noting it is custom | Yes if it contradicts a project constraint |

---

## Section 8 — Consequence Bridge

Before presenting the next question, write exactly **one sentence** naming the consequence of the just-made choice that motivates the next question.

Format:

> Because [previous decision outcome], the next blocking question is [topic].

This keeps the user oriented and makes the dependency chain visible.

---

## Section 9 — Running Decision Recap Format

Maintain a compact recap block, updated after every answered question. Place it at the **bottom of each response** after the current question. Do not re-explain previous decisions — list them in one line each.

```
---
**Decision Recap**
- Decision 1 — [Topic]: [chosen option] (recommended)
- Decision 2 — [Topic]: [chosen option] (deviation) ⚠️ Risk: [brief risk note]
- Decision 3 — [Topic]: [summary of custom answer] (custom)
- Contradiction 1 — [Topic]: [resolution] ⚠️ Contradiction: [conflicting requirement text]
```

Rules:
- Always include all decisions made so far, in order.
- Add ⚠️ + a brief risk note for any deviation or custom answer that introduces a constraint.
- When a chosen option conflicts with an explicitly stated requirement from the source design, mark it with `⚠️ Contradiction: [requirement text]` in addition to any deviation flag.
- Do not include unresolved decisions.

---

## Section 10 — Session-End Trigger

The interview session ends when:

- **(a)** No blocking unresolved questions remain — all decisions have been recorded in the recap.
- **(b)** The user says "done", "summarise", "that's enough", or equivalent.

When the session ends, produce the final summary (Section 11) immediately. Do not ask another question after the trigger.

---

## Section 11 — Final Summary Schema

The final summary contains exactly five sections, in this order:

### Chosen Direction

1–2 sentences describing the overall approach chosen across all decisions.

### Key Decisions

Numbered list of all recap entries, copied verbatim from the running recap.

### Rejected Alternatives

Table of options that were surfaced but not chosen:

| Option | Why Not Chosen |
|--------|---------------|
| [Option from any question] | [1–2 sentence reason] |

### Risks

Bulleted list of risks introduced by deviations or custom answers (from the ⚠️ entries in the recap). If no deviations or custom answers were made, write: "No deviations from recommended defaults — risk profile is minimal."

### Open Questions / Follow-up Work

Bulleted list of questions that were deferred, scoping issues that surfaced during the interview, or implementation details that were intentionally skipped. If none, write: "None identified."

When the interview resolved a wayfinding ticket, copy this summary into the ticket's Resolution section and add a one-line gist to the map's Decisions So Far table.

---

## Related Documents

- [00-05-wayfinder/SKILL.md](../00-05-wayfinder/SKILL.md) — invokes this skill to resolve `grilling`-type tickets on a wayfinding map
- [00-00-create-pr/SKILL.md](../00-00-create-pr/SKILL.md) — invokes this skill before finalizing an ambiguous implementation plan
- [00-01-implement-pr/SKILL.md](../00-01-implement-pr/SKILL.md) — invokes this skill before executing an ambiguous file/pattern plan
- [forge-upgrade-pr/SKILL.md](../forge-upgrade-pr/SKILL.md) — Forge migration knowledge to inspect before asking migration-related questions
