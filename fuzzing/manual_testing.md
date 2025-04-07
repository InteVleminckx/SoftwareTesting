# Manual testing

| Nr. | Test name                                                               | Exit Code | Error message                                    | Map                          | Action Sequence |
|-----|-------------------------------------------------------------------------|-----------|--------------------------------------------------|------------------------------|-----------------|
| 1   | Empty map                                                               | 10        | **** No lines                                    |                              | U               |
| 2   | One invalid character as map                                            | 10        | **** Unknown character                           | X                            | U               |   
| 3   | One valid character as map                                              | 10        | **** No player is set                            | W                            | U               |
| 4   | Only player as map                                                      | 10        | **** No food                                     | P                            | U               |
| 5   | Only food as map                                                        | 10        | **** No player is set                            | F                            | U               |
| 6   | Only player and food and start game                                     | 1         | Games pops up, but crashed afterwards            | FP                           | S               |
| 7   | player, food, and monster and start game                                | 1         | Games pops up, but crashed afterwards            | FPM                          | S               |
| 8   | Random characters on map                                                | 10        | **** Unknown character                           | &^@                          | S               |
| 9   | Valid map, random characters in sequence                                | /         | Game just pops up                                | WWWWW<br/>W0FPW<br/>WWWWW    | &é(^            |
| 10  | Valid map, random characters in sequence, but start with S              | 1         | Game just pops up, and crashes                   | WWWWW<br/>W0FPW<br/>WWWWW    | S&é(^           |
| 11  | Valid map with monster, random characters in sequence, but start with S | 1         | Game just pops up                                | WWWWW<br/>W0FPW<br/>WWWWW    | S&é(^           |
| 12  | One column on the map                                                   | /         | Game just pops up                                | F<br/>P<br/>M<br/>           | S               |
| 13  | One column on the map, with exit                                        | 0         | Game stops, player won                           | F<br/>P<br/>M<br/>           | SUE             |
| 14  | Valid map, actions without a S                                          | /         | Game doesn't do anything, still waiting to start | WWWWWW<br/>W0FPMW<br/>WWWWWW | LLLLRRRQQQ      |
| 15  | Valid map, actions without a S, with exit                               | 0         | Game stops                                       | WWWWWW<br/>W0FPMW<br/>WWWWWW | LLLLRRRQQQE     |
| 16  | Valid map, no actions                                                   | 0         | Game just pops up                                | WWWWWW<br/>W0FPMW<br/>WWWWWW |                 |
| 17  | Map with mismatched lines and one invalid character                     | 10        | **** Unknown character                           | WWWWWé<br/>W0FP<br/>WWWWWW   | SLE             |
| 18  | Map with mismatched lines and one invalid character                     | 10        | **** Widths mismatch                             | WWWWWW<br/>é0FP<br/>WWWWWW   | SLE             |
| 19  | Map with multiple players                                               | 10        | **** More than one player                        | WWWWWW<br/>W0PPMW<br/>WWWWWW | SLE             |
| 20  | Map with multiple players and mismatched line                           | 10        | **** More than one player                        | WWWWWW<br/>W0PPM<br/>WWWWWW  | SLE             |
| 21  | Map with multiple players and one invalid character                     | 10        | **** Unknown character                           | WWWWWW<br/>W0PéPM<br/>WWWWWW | SLE             |
| 22  | Map with empty first line                                               | 10        | **** No columns                                  | <br/>W0FPMW<br/>WWWWWW       | SLE             |