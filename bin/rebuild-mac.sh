# This is Mac OS only script for now, although it should not be that difficult to tailor to other *nix distros

# where to clone or update loom repo
LOOM_DIR=$1

[[ -z "${LOOM_DIR}" ]] && echo "Please specify where to clone or update loom repo" && exit 1

[[ ! -d "${LOOM_DIR}" ]] && echo "Directory does not exist: ${LOOM_DIR}" && exit 1
cd "${LOOM_DIR}" || exit 1
echo "PWD is: $(pwd)"
#GIT_CMD=""
#git status > /dev/null 2>&1
if ! git status > /dev/null 2>&1
then
  git clone https://github.com/openjdk/loom.git . || exit 1
else
  git pull
fi

rm -rf build
bash configure --with-boot-jdk=/Library/Java/JavaVirtualMachines/jdk-13.0.1.jdk/Contents/Home
#make clean
make images

DATE=$(date +%Y%m%d)
BUNDLE_HOME=build/macosx-x86_64-server-release/images/jdk-bundle
BUNDLE_DIR_NAME=$(ls "${BUNDLE_HOME}" | head -n 1)
BUNDLE_DIR_PATH=${BUNDLE_HOME}/${BUNDLE_DIR_NAME}
BUNDLE_DIR_BASE_NAME=${BUNDLE_DIR_NAME%.*}
BUNDLE_DIR_EXT=${BUNDLE_DIR_NAME##*.}
BUNDLE_DIR_NAME_NEW=${BUNDLE_DIR_BASE_NAME}-loom-$DATE.${BUNDLE_DIR_EXT}

JAVAS_HOME=/Library/Java/JavaVirtualMachines
JAVA_HOME=${JAVAS_HOME}/${BUNDLE_DIR_NAME}

sudo cp -R "${BUNDLE_DIR_PATH}/" "${JAVAS_HOME}/${BUNDLE_DIR_NAME_NEW}/"
sudo xattr -r -d com.apple.FinderInfo "${JAVAS_HOME}/${BUNDLE_DIR_NAME_NEW}/"

if [ -L "${JAVA_HOME}" ] && [ -d "${JAVA_HOME}" ]; then
  cd "${JAVAS_HOME}" || exit 1
  sudo rm "${JAVA_HOME}"
  sudo ln -sf "${BUNDLE_DIR_NAME_NEW}" "${BUNDLE_DIR_NAME}"
fi
