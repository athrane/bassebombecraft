---
description: 'Guidelines for creating prompt files for GitHub Copilot'
applyTo: '**/*.prompt.md'
---

# Prompt Files Guidelines

## Required Frontmatter
```yaml
---
description: 'Actionable outcome in one sentence'
mode: 'ask | edit | agent'
tools: [minimal tool set needed]
---
```

Optional: `model` (when specific capability needed)

## File Structure
1. **Title**: `#` heading matching intent
2. **Mission/Directive**: What to accomplish
3. **Scope & Preconditions**: Context required
4. **Inputs**: Variables with `${input:name[:placeholder]}`
5. **Workflow**: Step-by-step actions
6. **Output**: Format, location, success criteria
7. **Validation**: How to verify results

## Input Variables
```markdown
${input:fileName:Enter filename}
${selection}
${file}
${workspaceFolder}
```

Document how to handle missing inputs.

## Writing Style
- Direct imperative sentences: "Analyze", "Generate", "Summarize"
- Short, unambiguous sentences
- Avoid idioms and culturally-specific references

## Tools
- List minimum required tools
- State preferred execution order if it matters
- Warn about destructive operations

## Quality Checklist
- [ ] Frontmatter complete and least-privilege
- [ ] Inputs have defaults/fallbacks
- [ ] Workflow has no gaps
- [ ] Output format specified
- [ ] Validation steps actionable
- [ ] Tested in VS Code

## Location & Naming
- `.github/prompts/` directory
- `kebab-case-action.prompt.md`
