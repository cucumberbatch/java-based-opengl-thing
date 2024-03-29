# Game engine dev tasks to do
- [ ] XML scene file loader
  - [ ] XML format of scene
  - [ ] Serialization/deserialization for various class versions
- [ ] Scene object loader
- [ ] Editor
  - [ ] Multifunctional editor cursor (OpenGL window)
    - [ ] Editor scene game object cursor selection
    - [ ] Editor cursor states
  - [ ] Separate editor window (Swing window)
    - [ ] Event bus
    - [ ] Console log
    - [ ] Inspector window (entities and components all in one)
    - [ ] Command line ?
- [ ] Physics improvements
  - [ ] Chop world scene into squares/partitions and store them in tree (QuadTree/Octree)
- [ ] Graphics API
  - [x] Pass into `render()` system method an abstract `Graphics` object instead of `Window`
  - [ ] Implement OpenGL graphics engine API
    - [ ] Solve a multiple textures problem
      - When we try to render multiple objects with different textures or single object with several
        texture (multiplication/addition modes) then we see only one texture
  - [ ] Implement OpenGL ES graphics engine API (Android API)
  - [ ] **(optional)** Implement Swing/AWT CPU graphics engine API (I think we don't need this)
- [ ] Network
  - [ ] TCP/UDP sockets API for fast game data transferring
  - [ ] Serialization/deserialization of game data for network transferring
- [ ] Sounds
  - [ ] Sounds and music controller API
  - [ ] Different kinds of hardware audio API support
- [ ] Cross-platform-ness
  - [ ] Create Maven profiles
    - [ ] Windows
    - [ ] Linux
    - [ ] Android
- [ ] Optimization
  - [ ] A very memory expensive logger!!! Needs to rework or even replace with log4j2 + slf4j
  - [ ] Improve system handling performance (expensive iterations on systems/components in map)
- [ ] Logger
  - [ ] A very memory expensive logger!!! See the checkbox above in the 'Optimization' section
  - [ ] Reimplement logger based on standard Java logger interface
  - [ ] Print log info into `PrintWriter` (not `System.out`)
  - [ ] Use `printStackTrace` for printing exception stack traces