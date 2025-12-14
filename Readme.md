Notes during developing

- Actual api doesn't support pagination, therefore I delete paging library deps.
- Nor api provides with ids for items, that's super strange, it forces to autogenerate them in
  Room.
- Also I can't but mention reason of fetching detailed objects for screen, where they actually not
  required.
  One of your requirements was the entire app working without the internet. Making 2 queries (first
  for the list
  of not detailed items, and second for one detailed) would allow only the list working without the
  internet.
  also it seems unsafe fetching the item not by id, but by anything else. 