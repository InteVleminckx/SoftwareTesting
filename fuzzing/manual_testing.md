# Manual testing

| Test name                                                               | Exit Code | Error message                                    | Map                          | Action Sequence |
|-------------------------------------------------------------------------|-----------|--------------------------------------------------|------------------------------|-----------------|
| Empty map                                                               | 10        | **** No lines                                    |                              | U               |
| One invalid character as map                                            | 10        | **** Unknown character                           | X                            | U               |   
| One valid character as map                                              | 10        | **** No player is set                            | W                            | U               |
| Only player as map                                                      | 10        | **** No food                                     | P                            | U               |
| Only food as map                                                        | 10        | **** No player is set                            | F                            | U               |
| Only player and food and start game                                     | 1         | Games pops up, but crashed afterwards            | FP                           | S               |
| player, food, and monster and start game                                | 1         | Games pops up, but crashed afterwards            | FPM                          | S               |
| Random characters on map                                                | 10        | **** Unknown character                           | &^@                          | S               |
| Valid map, random characters in sequence                                | /         | Game just pops up                                | WWWWW<br/>W0FPW<br/>WWWWW    | &é(^            |
| Valid map, random characters in sequence, but start with S              | 1         | Game just pops up, and crashes                   | WWWWW<br/>W0FPW<br/>WWWWW    | S&é(^           |
| Valid map with monster, random characters in sequence, but start with S | 1         | Game just pops up                                | WWWWW<br/>W0FPW<br/>WWWWW    | S&é(^           |
| One column on the map                                                   | /         | Game just pops up                                | F<br/>P<br/>M<br/>           | S               |
| One column on the map, with exit                                        | 0         | Game stops, player won                           | F<br/>P<br/>M<br/>           | SUE             |
| Valid map, actions without a S                                          | /         | Game doesn't do anything, still waiting to start | WWWWWW<br/>W0FPMW<br/>WWWWWW | LLLLRRRQQQ      |
| Valid map, actions without a S, with exit                               | 0         | Game stops                                       | WWWWWW<br/>W0FPMW<br/>WWWWWW | LLLLRRRQQQE     |
| Valid map, no actions                                                   | 0         | Game just pops up                                | WWWWWW<br/>W0FPMW<br/>WWWWWW |                 |
| Map with mismatched lines and one invalid character                     | 10        | **** Unknown character                           | WWWWWé<br/>W0FP<br/>WWWWWW   | SLE             |
| Map with multiple players                                               | 10        | **** More than one player                        | WWWWWé<br/>W0PPMW<br/>WWWWWW | SLE             |