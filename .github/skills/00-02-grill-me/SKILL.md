---
name: 00-02-grill-me
description: 'Option-driven design interview to stress-test a plan before implementation: one blocking question, concrete options, recommended default. Use when grilling a plan or design for the BasseBombeCraft Minecraft Forge mod.'
argument-hint: '[plan-or-design-to-grill]'
---

# Grill-Me Skill -- Option-Driven Design Interview

This skill governs the structured interview workflow used to stress-test a plan or design before implementation. It replaces open-ended dialogue with a one-question-at-a-time interview where each question surfaces the next blocking decision, presents concrete options, marks the recommended default, and records the outcome before moving on.

## When to Use This Skill

Load this skill when:

- The user says "grill me on this plan", "challenge my design", "help me think through this", or similar
- The user has a plan or design they want probed before implementation begins
- The user asks you to surface weaknesses, probe assumptions, or stress-test an approach
- A planning session benefits from structured decision capture rather than free-form discussion

> **Scope**: This skill governs interview _flow_ only -- not domain knowledge. Domain knowledge (Forge APIs, operator pattern, registered-items pattern, TypeUtils-style validation, etc.) is supplied by inspecting the codebase via the `Explore` subagent or file reads. Typical usage: read 2-3 existing `.github/.requirements/pr/14XX-*.md` files in the same domain for phrasing conventions, then invoke `00-02-grill-me` to probe the plan for weaknesses before starting implementation.
>
> **Integration with the development loop**: `00-02-grill-me` is invoked from within each of the other `00-*` workflow skills, not just on direct user request:
> - [`wayfinder`](../wayfinder/SKILL.md) -- invoked to resolve `grilling`-type tickets on a wayfinding map; if a grill-me session keeps surfacing new blocking dependencies instead of converging, that's the signal to chart a map with `wayfinder` instead of continuing the interview
> - [`00-00-create-pr`](../00-00-create-pr/SKILL.md) -- before finalizing a PR description whose Implementation Plan involves significant or ambiguous design decisions
> - [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md) -- before beginning execution, when the file/pattern plan involves significant architectural decisions or ambiguity
> - [`00-03-review-pr`](../00-03-review-pr/SKILL.md) -- when a knowledge-capture candidate has more than one viable capture approach

---

## Section 1 -- Inspection-First Rule

**Before asking the user anything**, search code, docs, and open files for relevant context.

- If the answer to a potential question is already derivable from context (existing implementation in `src/main/java/bassebombecraft/`, docs, prior decisions in the conversation, an existing `.github/.requirements/pr/14XX-*.md` file in the same domain), record it in the recap and **skip the question**.
- Only ask about decisions that are genuinely unresolved after inspection.
- State what you found during inspection at the start of the session: "I inspected X and Y. The following decisions are already resolved: ..."

This prevents asking the user to re-explain things that are already documented in the codebase.

---

## Section 1.5 -- Contradiction Scan

**Before asking Decision 1**, scan the plan or design for pairs of requirements that are mutually exclusive or directly contradictory.

- Produce an explicit bulleted list titled **"Contradictions found:"** with one line per conflict, naming the two conflicting requirements and the nature of the conflict.
- If no contradictions exist, write **"No contradictions detected."** and proceed to Section 2.
- Contradictions are treated the same as unresolved decisions: each one is recorded in the recap (Section 9) as a `WARNING Contradiction:` entry when resolved.

---

## Section 2 -- Blocking-Question Identification

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

## Section 3 -- Selectable-UI Preference

When `vscode_askQuestions` is available, use it for every interview question.

Requirements for each call:

| Field | Rule |
|-------|------|
| `question` | State the decision being made -- not a yes/no question. **Must be at most 200 characters.** If the generated question exceeds this, rephrase to fit before sending. |
| `options` | 2-5 concrete alternatives (see Section 5 for quality criteria) |
| `recommended: true` | Mark exactly one option as the recommended default |
| `allowFreeformInput` | Always set to `true` |
| `header` | Unique per call. Use pattern `Decision N -- <short topic>`. **Must be at most 50 characters.** If the generated header exceeds, shorten the topic phrase before sending. |
| non-recommended options | Every option **not** marked `recommended: true` must include `description: "Deviation from recommended default"` so the user sees a visible signal that choosing it diverges from the advised path. |

After the selectable question, add the recommended-option rationale (Section 6) as prose below the tool call.

---

## Section 4 -- Plain-Markdown Fallback

When `vscode_askQuestions` is **not available**, render the question as:

```text
**Decision N -- [Topic]**

[Decision statement]

1. [Option A]
2. STAR [Option B] _(recommended)_
3. [Option C]

_Or describe your own approach._
```

Rules:

- Mark exactly one option with STAR and `_(recommended)_`.
- Include the explicit sentence: "Or describe your own approach." to invite custom input.
- Follow with the recommended-option rationale (Section 6).

---

## Section 5 -- Option-Quality Criteria

Each option presented in a question must satisfy all five criteria:

1. **Concrete and actionable** -- The option names a specific approach, not a vague direction (e.g. "Use `RegisteredEffects.bootstrap()` called from `BassebombeCraft.setup()`" not "Use a registry").
2. **Materially different in consequence** -- Choosing it must lead to observably different implementation decisions downstream. Do not present options that are effectively synonyms.
3. **Decision-shaping** -- Choosing it changes at least one later decision in the interview. Options that have no downstream effect are not blocking and should not be surfaced.
4. **Same level of abstraction** -- Do not mix architectural-level options with implementation-detail options in the same question.
5. **Free of obvious dominance** -- No option should be trivially better on all dimensions. If one option dominates, merge it with the recommended option and remove the dominated one.

---

## Section 6 -- Recommended-Option Rationale

After each question (in both UI modes), provide exactly **1-2 sentences** explaining:

1. Why the recommended default is the best starting point for this plan.
2. Its key assumption and the main tradeoff accepted by choosing it.

Format:

> **Why STAR [Recommended Option]**: [1-2 sentences.] Key assumption: [assumption]. Main tradeoff: [tradeoff].

---

## Section 7 -- Branching Behaviour Table

| User's answer | Record in recap | Note deviation? | Flag risk? |
|--------------|-----------------|-----------------|------------|
| Recommended option chosen | `Decision N -- [topic]: [option] (recommended)` | No | No |
| Non-recommended option chosen | `Decision N -- [topic]: [option] (deviation)` | Yes -- one sentence why this deviates | Yes -- name the risk introduced |
| Custom / freeform answer | `Decision N -- [topic]: [summary of custom answer] (custom)` | Yes -- one sentence noting it is custom | Yes if it contradicts a project constraint |

---

## Section 8 -- Consequence Bridge

Before presenting the next question, write exactly **one sentence** naming the consequence of the just-made choice that motivates the next question.

Format:

> Because [previous decision outcome], the next blocking question is [topic].

This keeps the user oriented and makes the dependency chain visible.

---

## Section 9 -- Running Decision Recap Format

Maintain a compact recap block, updated after every answered question. Place it at the **bottom of each response** after the current question. Do not re-explain previous decisions -- list them in one line each.

```text
---
**Decision Recap**
- Decision 1 -- [Topic]: [chosen option] (recommended)
- Decision 2 -- [Topic]: [chosen option] (deviation) WARNING Risk: [brief risk note]
- Decision 3 -- [Topic]: [summary of custom answer] (custom)
- Contradiction 1 -- [Topic]: [resolution] WARNING Contradiction: [conflicting requirement text]
```

Rules:

- Always include all decisions made so far, in order.
- Add WARNING + a brief risk note for any deviation or custom answer that introduces a constraint.
- When a chosen option conflicts with an explicitly stated requirement from the source design, mark it with `WARNING Contradiction: [requirement text]` in addition to any deviation flag.
- Do not include unresolved decisions.

---

## Section 10 -- Session-End Trigger

The interview session ends when:

- **(a)** No blocking unresolved questions remain -- all decisions have been recorded in the recap.
- **(b)** The user says "done", "summarise", "that's enough", or equivalent.

When the session ends, produce the final summary (Section 11) immediately. Do not ask another question after the trigger.

---

## Section 11 -- Final Summary Schema

The final summary contains exactly five sections, in this order:

### Chosen Direction

1-2 sentences describing the overall approach chosen across all decisions.

### Key Decisions

Numbered list of all recap entries, copied verbatim from the running recap.

### Rejected Alternatives

Table of options that were surfaced but not chosen:

| Option | Why Not Chosen |
|--------|---------------|
| [Option from any question] | [1-2 sentence reason] |

### Risks

Bulleted list of risks introduced by deviations or custom answers (from the WARNING entries in the recap). If no deviations or custom answers were made, write: "No deviations from recommended defaults -- risk profile is minimal."

### Open Questions / Follow-up Work

Bulleted list of questions that were deferred, scoping issues that surfaced during the interview, or implementation details that were intentionally skipped. If none, write: "None identified."

---

## Related Documents

- [`wayfinder/SKILL.md`](../wayfinder/SKILL.md) -- invokes this skill to resolve `grilling`-type tickets on a wayfinding map; if a grill-me session keeps surfacing new blocking dependencies, that's the signal to chart a map with wayfinder instead
- [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) -- invokes this skill before finalizing an ambiguous implementation plan (Step 6)
- [`00-01-implement-pr/SKILL.md`](../00-01-implement-pr/SKILL.md) -- invokes this skill before executing an ambiguous file/pattern plan (Phase 2 Step 8)
- [`00-03-review-pr/SKILL.md`](../00-03-review-pr/SKILL.md) -- invokes this skill when a knowledge-capture candidate has multiple viable approaches (Steps 5/6)
- [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) -- the PR spec template (used for the final summary's risk/open-questions sections)
- [`.github/.requirements/pr/`](../../.requirements/pr/) -- past PR specs to absorb phrasing conventions from
- Original pattern: [mattpocock/skills](https://github.com/mattpocock/skills) -- adapted from the Herodotus copy at `.github/skills/00-02-grill-me copy/SKILL.md`