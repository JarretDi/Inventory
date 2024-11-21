# TTRPG Character/Inventory management system

## What is this application?
This application will provide an organized way to add, view and filter characters, items, etc. for games like D&D and Pathfinder, which are traditionally done pen and paper, but lately things like laptops and tablets are used to more effectively keep track of information within the game. Ideally, this is meant to be used by players who have a difficult time keeping track of what exactly is in their inventory, but also provides an easy way for organized players to save some time as well. I found that through playing various games, the main way to keep track of these things is through noting them down manually in a text editor, which often requires those small notes to be referenced in the rulebook *every single* time for *each item* a player wants to use. This project is an attempt to remedy this by sorting items by categories (i.e. consumable, weapon, armour) so that the information represented by each item is both easy to find and easy to access.

## User Stories
- As a user, I want to be able to add a number of items into my inventory
- As a user, I want to be able to view all items previously entered into the inventory, ideally with some way to sort them by name, category, price, etc.
- As a user, I want to be able to have a way to easily throw away and discard items in groups without having to manually discard them one-by-one
- As a user, I want to be able to favorite commonly used items to be able to access them quicker
- As a user, I want to be able to easily transfer items from one character to another
- As a user, I want to be able to minimize long descriptions of items and being able to reopen them to save space on the screen
- As a user, I want to be able to specify a quantity of identical items rather than having the same item be added in two separate instances
- As a user, I want to be able to save my inventory under a 'character' as well as all items within it, preserving sort order
- As a user, I want to be able to load data from different characters' inventories

# Instructions for End User

- You can add an item into an inventory by:
    - Clicking the "+Add" button in the left control panel, causing a new panel to be created
    - Customize the item to your liking, including a Type, Name, Value, Weight, Quantity and Description
    - Clicking the "Add" button on the newly created panel to add your finished item to the inventory panel on the right
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by sorting the list of items
    - On the bottom of the right-hand panel are buttons that will change the sort of the inventory
    - Currently, it includes name, type, value and weight
    - Clicking one of those buttons will cause the inventory to be sorted by that parameter descending, and clicking it again will result in it sorting ascending
    - Note that all starred items are shown first, regardless of the sort, but they are sorted within all the starred items
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by editing and viewing items
    - Once an item has been created, you can see a pencil marker, generally to the right of each item
    - Clicking that edit button results in the opening of the Item Editor window, in which you can change a previously added item
    - Additionally, this is the menu used for viewing item descriptions, and can be moved off to the side in case one wants to keep the information opened
    - Note that if quantity is set to a <1 value, it removes the item. This is intended behaviour
    - Once you have finished viewing the item, you may either click the x on the window to not apply changes, or the edit button to keep them
- You can locate my visual component by:
    - Opening the program, there are icons for almost every button
    - Additionally, when an inventory is sorted, an icon next to the sort buttons appears that describes the sort
    - Additionally, when an item is added, it is displayed with an image with respect to its type
    - Additionally, if an item is favourited, it will appear with a star next to it
- You can save the state of my application by:
    - Clicking the save button in the left hand control panel
    - Entering a name to save it under
    - Confirming the name
- You can reload the state of my application by:
    - Clicking the load button in the left hand control panel
    - A confirmation may pop up to confirm if you want to continue
    - Choosing a character out of the files in the combo box
    - Confirming the inventory to load

## Credit for images used:

Icons: Iconoir by Luca Burgio "https://icon-sets.iconify.design/iconoir/"

Sword: <a href='https://pngtree.com/freepng/sword_6045741.html'>png image from pngtree.com/</a>

Shield: <a href="https://www.freepik.com/free-vector/wooden-shield-with-metal-frame-blank-wood-panel_30700134.htm#fromView=search&page=1&position=1&uuid=2966be39-db18-47a0-9f78-59579db7f247&new_detail=true">Image by upklyak on Freepik</a>

Potion: <a href='https://pngtree.com/freepng/green-potion-glass-bottle_7485721.html'>png image from pngtree.com/</a>

Gold: Image by <a href="https://pixabay.com/users/19dulce91-1429737/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=1590337">Dulce</a> from <a href="https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=1590337">Pixabay</a>

Scrolls: <a href="https://www.freepik.com/free-vector/old-magic-book-with-parchment-pages-scroll_234684286.htm#fromView=search&page=1&position=8&uuid=f5c2af30-2d8e-4099-bf88-496e1dedeaa8&new_detail=true">Image by upklyak on Freepik</a>
