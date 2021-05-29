# DarkCitizens

## What is this supposed to do?
DarkRP is a game mode in Garry's Mod. It lets players join the server, become a citizen of a fictional city and take a job. Jobs are typically the following:


- Mayor: Decides the laws of the city and sets taxes for jobs. Upon death, an election is triggered for the job of Mayor. Players can pay $XXX to take part in the election.
- Police officer/Government jobs: Protects the city from criminals. Can raid peoples properties if found doing illegal activity and arrest people, sending them to jail.
- Mobster: Illegally prints money, can mug people, can raid rival mobsters, can kill the mayor if the laws are too much/taxes are too high, rob the bank.
- Doctor: Heals people for money
- Baker: Can spawn in a bakery factory and cook bread to sell
- Miner: Can mine ores and sell them for money

Of course, it is your plugin now, do whatever you want with it, and what you think is best for your server ;D

## So how do I configure this thing?
So you want to configure the plugin huh? First put the plugin into your plugins directory and start up your server, the plugin will
generate a DarkCitizens directory, if you take a look into the directory you will see a data.yml file
and a config.yml. You should not touch the data.yml file, and if you do, only if the server is not running.
I will explain what and how you can do something with it.
Let's first concentrate on the config.yml file, if you just want a quickstart, and you are confident that you will figure it out, there is 
the default config in the file, so go for it.
### Groups
So let's start by defining some groups. Every job belongs to a group. By joining a Job you can make Xp
that will be added to the group the job is in, so we can define a maximum level and the level thresholds in it. The 
group also defines if the players can be criminals and if the player damage each other inside the group.
To define the group section we use the `Group` tag. Under this tag we define the groups' identifier(name) and then its properties.  
The properties are:  
- `maxLvl` The maximum level you can reach in this group  
- `lvlThresholds` A list with the xp thresholds you need to get to the  next level, if you define fewer levels then your max level, or just leave away this tag
the list will be filled with steps of 50
- `friendlyFire`  If members of this group can damage each other
- `canBeCriminal`  If members of this group can become criminals
- `prefix` A prefix that will be registered at the Placeholder API, you can get it using the `group_prefix` placeholder
An group section may look something like this:
```YAML
Groups:
  Civilian:
  maxLvl: 10
  friendlyFire: true
  canBeCriminal: true
  prefix: "&1[Civ]"

  Government:
    maxLvl: 10
    friendlyFire: false
    canBeCriminal: false
    lvlThresholds: [50, 100, 200, 400, 500, 600, 700, 900, 1000, 1500]
    prefix: "[Gov]"
```
### Actions
Okay next on we define some actions we can complete, there are 4(actually 5) actions types,
- ChopWood
- CraftItem
- Fish
- MineBlock
- DEBUG(A debug task, you shouldn't use this)

Each type comes with different configuration options, all actions need to be defined under the `Actions` tag and every action 
needs the `type` tag as well as a `name` and a `description`.  
So let's take a look at the configuration options for each type:  
The ChopWood action needs the `number` tag which defines the number of block you have to chop down to complete this action.
The CraftItem action needs the `itemType` and the `number` tag from above. The item type defines which item needs to be crafted to complete this.
Next on is the Fish action, this action only needs the `number` tag.
Last of all we have the MineBlock action, this block needs the `blockType` to define which block to mine, and who guessed it,
the `number` tag.
So to make it clear I'll show you an example configuration with every action:
```YAML
Actions:
    chop_25:
        name: Chop Wood
        description: Chop 25 wood
        type: ChopWood
        number: 25
    craft_sticks:
        name: STICKSSSS
        description: Craft 25 sticks
        type: CraftItem
        number: 25
        itemType: STICK
    fish:
        name: Go Fishing
        description: Fish 25 fish
        number: 25
    mineIron:
        name: Mine Iron
        description: Mine 25 iron
        type: MineBlock
        number: 25
        blockType: IRON_ORE
```
To get a list of the itemType and blockType visit the [Spigot Website](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html).
Okay next on we use the actions in our tasks, if all actions in a task are completed the task will be completed.
### Tasks
We define tasks under the task section defined by the `Tasks` tag.
Every task is made of an `income`, `xp`, a list of `actions`, a `description` and at last an `icon`.
So a task section may look like this:
```YAML
Tasks:
  Mine Stuff:
    income: 10
    xp: 10
    actions: [Mine Iron, Mine Stone]
    description: "Mine 25 Iron and Stone"
    icon: IRON_ORE
```
### Jobs
Okay now we have actions wrapped in tasks and our groups. We have everything we need to define a job, so let's do that.
Every job needs to be defined under the `Jobs` section. 
Let's take a look at the options we have to configure one:
- `group` The group this job belongs to
- `playerLimit` The maximum amount of player that can be in this job
- `tasks` The tasks this job has
- `canDemote`  Other jobs this job can demote players from
- `baseIncome` The base income that will be paid every n minutes
- `baseXp` The base xp that will be paid every n minutes
- `minLvl` The minimum level to need to join this group
- `electionRequired` If an election is required to join this job
- `permissionRequired` If you need permissions to join this job
- `icon` The icon to display in the GUI
- `candidateTime` How long the time-span is in which you can candidate for this job
- `voteTime` How long you can vote 
- `candidateFee` How much it costs to candidate
- `voteFee` How much it costs to vote
- `leaveOnDeath` If the player should be kicked from the job when dying
- `isMajor` If the job is a mayor
- `prefix` The prefix for this job registered at the Placeholder API, (Access via `job_prefix`)

Ignore the candidate and vote stuff if the job isn't elected. So let's define some example jobs:
```YAML
Jobs:
  Mayor:
    group: Government
    playerLimit: 1
    tasks: []
    canDemote: [Police Officer]
    baseIncome: 250
    baseXp: 50
    minLvl: 5
    electionRequired: true
    permissionRequired: false
    icon: PLAYER_HEAD
    candidateTime: 2
    voteTime: 2
    candidateFee: 100
    voteFee: 50
    leaveOnDeath: true
    isMajor: true
    prefix: "&1[Mayor]"
  
  Police Officer:
    group: Government
    playerLimit: 2
    tasks: [STICKSSS]
    canDemote: []
    baseIncome: 100
    baseXp: 30
    minLvl: 2
    electionRequired: false
    permissionRequired: false
    icon: IRON_SWORD
    leaveOnDeath: false
    isMajor: false
    prefix: "[Officer]"
```
### Other Config Options
At the moment there is one thing we can configure additionally, the time at which the base income and base xp is paid out.
The `baseIncomeTime` tag followed by the minutes after which the jobs should be paid. Eg:
```YAML
baseIncomeTime: 5
```
Would set the interval to 5 minutes and so every 5 minutes everyone with a job will be paid the base income the base XP.

## Permissions
Permissions enable you to give different rights/possibility to different players.
```yaml
  drp.*:
    description: Access ALL DarkRp commands

  drp.job:
    description: Access all basic job commands
    
  drp.tasks:
    description: Access all basic task commands
    
  drp.job.*:
    description: Join all jobs, where a permission is required
    
  drp.vote:
    description: Show all elections and vote for a candidate

  drp.laws.taxes:
    description: Set taxes when in a job that is taged with mayor

  drp.laws.create:
    description: Makes it possible to create a new law when in a job taged with mayor

  drp.laws.remove:
    description: Makes it possible to remove an existing law when in a job taged with mayor

  drp.laws.show:
    description: Makes it possible to see all laws that are currently available
```
Normally a player should have access to all these permissions, except the `drp.*` and maybe the `drp.job.*`, therefore
all except these are default permissions each player will gain. If you to exclude someone from these certain actions,
for eg. don't let him open the job menu just disable the `drp.job` permission for him. 
The only really interesting permission here are the ones that enable/dissable players to join certain jobs.
To make a job exclusive to these permissions use the `permissionRequired` tag in the job config. And then give 
the permission `drp.job.<jobname>` permission to the player, for eg. you defined an exclusive `policeman` job.
The permission will be `drp.job.policeman`

## Additional Dependencies
To get this Plugin to run you will need Vault, RecraftedCore and a Plugin providing an economy for Vault.

## FAQ
### Wait I don't understand exactly how XP works
So, Xp can be earned through completing tasks or just the base income(base XP) every job has. The Xp will be added to the group
the job is in which the player completed the task from. So for example if you have a cop job which is in the group Government and earn 5 xp,
it will be added to the group Government. If the player reaches the next Xp threshold he will level up this groups level and 
maybe have new access to new jobs.

### Elections, what the hell are these and how do they work? I live in a Fascist country!!
Okay, elections are a process in which the players select the player they want in the job from the list of candidates.
Every player that fulfills the requirements to join the job and has enough money to pay the candidate fee, can candidate.
An election will have a candidate time, in which the player who want the job can list themselves as candidate followed by
an election time, in which everyone who can pay the fee can vote for their candidate.

### Can I have some more additions for this plugin it is not enough for me?
Check out my GitHub profile I have a few other plugins that will help you to add more content. Typically, those plugins
which work with this one, will start with a Dark followed by a description of what they do.

### I have more questions, and I don't know what tf to do.
Nothing easier than that, contact me on Discord DarkVanityOfLight#8817, write me an email darkvanity.oflight@gmail.com or simply
open up an issue here on my GitHub, I ll try to answer as fast as I can.

## I hope I forgot nothing, if I did, you will figure it out or ask me