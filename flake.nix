{
  description = "Saturn Client";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };

        jdk = pkgs.jdk21;

        gradle = pkgs.gradle.override {
          java = jdk;
          javaToolchains = [ jdk ];
        };

        runtimeLibs = with pkgs; [
          glfw
          openal
          libGL
          vulkan-loader
          xorg.libX11
          xorg.libXcursor
          xorg.libXrandr
          xorg.libXxf86vm
          xorg.libXi
          xorg.libXext
          xorg.libXrender
        ];
      in
      {
        devShells.default = pkgs.mkShell {
          packages = [
            gradle
            jdk
          ];

          JAVA_HOME = "${jdk}";

          LD_LIBRARY_PATH =
            pkgs.lib.optionalString pkgs.stdenv.isLinux
              (pkgs.lib.makeLibraryPath runtimeLibs);
        };
      });
}