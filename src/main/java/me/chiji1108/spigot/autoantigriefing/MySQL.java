/*
* プログラミング初心者なので悪しからず
* */

package me.chiji1108.spigot.autoantigriefing;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.time.LocalTime;

public class MySQL implements Listener,CommandExecutor {
    private Connection connection;
    private String host, database, username, password, users, logins, worlds, block_places, block_breaks;
    private int port;
    public String cmd1 = "gp";

    private Plugin plugin = AutoAntiGriefing.getPlugin(AutoAntiGriefing.class);

    public MySQL() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        host = plugin.getConfig().getString("MySQL.host");
        port = plugin.getConfig().getInt("MySQL.port");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.username");
        password = plugin.getConfig().getString("MySQL.password");
        users = "users";
        logins = "logins";
        worlds = "worlds";
        block_places = "block_places";
        block_breaks = "block_breaks";
    }

    //ここからMySQLに関するメソッド
    synchronized public void openConnection() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
            plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "MySQLのデータベースに接続しました: " + ChatColor.WHITE + this.database);
        } catch (ClassNotFoundException e) {
            plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + ChatColor.RED + "JDBCドライバのロードに失敗しました");
            e.printStackTrace();
        } catch (SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + ChatColor.RED + "MySQLに接続できませんでした。");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                getConnection().close();
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "MySQLのデータベースから切断します");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void createTables() {
        try {
            String sql =
                    "-- Server version\t5.7.19\n" +
                    "\n" +
                    "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
                    "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
                    "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
                    "/*!40101 SET NAMES utf8 */;\n" +
                    "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
                    "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
                    "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
                    "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
                    "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
                    "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
                    "\n" +
                    "--\n" +
                    "-- Table structure for table `block_breaks`\n" +
                    "--\n" +
                    "\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;\n" +
                    "CREATE TABLE IF NOT EXISTS `block_breaks` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `user_id` int(11) NOT NULL,\n" +
                    "  `world_id` int(11) NOT NULL,\n" +
                    "  `X` int(11) NOT NULL,\n" +
                    "  `Y` int(11) NOT NULL,\n" +
                    "  `Z` int(11) NOT NULL,\n" +
                    "  `block_id` int(11) NOT NULL,\n" +
                    "  `block_subid` int(11) NOT NULL,\n" +
                    "  `time` datetime NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
                    "\n" +
                    "--\n" +
                    "-- Table structure for table `block_places`\n" +
                    "--\n" +
                    "\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;\n" +
                    "CREATE TABLE IF NOT EXISTS `block_places` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `user_id` int(11) NOT NULL,\n" +
                    "  `world_id` int(11) NOT NULL,\n" +
                    "  `x` int(11) NOT NULL,\n" +
                    "  `y` int(11) NOT NULL,\n" +
                    "  `z` int(11) NOT NULL,\n" +
                    "  `block_id` int(11) NOT NULL,\n" +
                    "  `block_subid` int(11) NOT NULL,\n" +
                    "  `time` datetime NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
                    "\n" +
                    "--\n" +
                    "-- Table structure for table `logins`\n" +
                    "--\n" +
                    "\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;\n" +
                    "CREATE TABLE IF NOT EXISTS `logins` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `user_id` int(11) NOT NULL,\n" +
                    "  `DateTime` datetime NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
                    "\n" +
                    "--\n" +
                    "-- Table structure for table `users`\n" +
                    "--\n" +
                    "\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;\n" +
                    "CREATE TABLE IF NOT EXISTS `users` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `uuid` varchar(255) NOT NULL,\n" +
                    "  `name` varchar(255) NOT NULL,\n" +
                    "  `GP` double unsigned NOT NULL,\n" +
                    "  `login_times` int(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`),\n" +
                    "  UNIQUE KEY `uuid_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
                    "\n" +
                    "--\n" +
                    "-- Table structure for table `worlds`\n" +
                    "--\n" +
                    "\n" +
                    "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
                    "/*!40101 SET character_set_client = utf8 */;\n" +
                    "CREATE TABLE IF NOT EXISTS `worlds` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
                    "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
                    "/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;\n" +
                    "\n" +
                    "/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" +
                    "/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" +
                    "/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" +
                    "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
                    "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" +
                    "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" +
                    "/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;";

            Statement statement = getConnection().createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ここからデータベース操作
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World w = player.getWorld();
        createPlayer(player);
        addLoginLog(player);
        noLoginCount(player);
        createWorld(w);
    }

    public void createPlayer(Player player) {
        try {
            if (!(playerExists(player))) { //そのプレイヤーがいない時
                String name = player.getName();
                String uuid = player.getUniqueId().toString();
                String sql = "INSERT INTO " + users + " (uuid,name,GP,login_times) VALUES (?,?,?,?)";
                PreparedStatement statement = getConnection().prepareStatement(sql);
                statement.setString(1, uuid);
                statement.setString(2, name);
                statement.setDouble(3, 0);
                statement.setInt(4, 0);
                statement.executeUpdate();
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "初回ログインのためプレイヤーデータを作成しました: " + ChatColor.WHITE + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLoginLog(Player player) {
        try {
            String name = player.getName();
            String uuid = player.getUniqueId().toString();
            int user_id = callUsersId(player);

            String sql1 = "UPDATE " + users + " SET login_times=login_times+1 WHERE UUID=?";
            PreparedStatement statement1 = getConnection().prepareStatement(sql1);
            statement1.setString(1, uuid);
            statement1.executeUpdate();
            plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "ログイン回数を+1しました: " + ChatColor.WHITE + name);

            String sql2 = "INSERT INTO " + logins + " (user_id,DateTime) VALUES (?,now())";
            PreparedStatement statement2 = getConnection().prepareStatement(sql2);
            statement2.setInt(1, user_id);
            statement2.executeUpdate();
            plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "ログインした現在の時間を記録しました: " +ChatColor.WHITE + name);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void noLoginCount(Player player) { //1分以内の再ログインはカウントしない
        try {
            if (callTrueLoginTimes(player) >= 2) {
                long dateTimeTo = callLatestLoginDateTime(player).getTime();
                long dateTimeFrom = callSecondLoginDateTime(player).getTime();
                // 差分の時間を算出します。
                long DiffMinutes = ( dateTimeTo - dateTimeFrom ) / (1000 * 60);

                if (DiffMinutes < 1) {
                    String name = player.getName();
                    int user_id = callUsersId(player);

                    String sql = "UPDATE " + users + " SET login_times=login_times-1 WHERE id=?";
                    PreparedStatement statement = getConnection().prepareStatement(sql);
                    statement.setInt(1, user_id);
                    statement.executeUpdate();
                    plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "1分以内の再ログインのためログイン回数を-1しました : " + ChatColor.WHITE + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWorld(World world) {
        try {
            String name = world.getName();

            if (!(worldsExists(world))) { //そのワールドがいない時
                String sql = "INSERT INTO " + worlds + " (name) VALUES (?)";
                PreparedStatement statement = getConnection().prepareStatement(sql);
                statement.setString(1, name);
                statement.executeUpdate();
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "初回ロードのためワールドデータを作成しました: " + ChatColor.WHITE + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void addPlaceLog(BlockPlaceEvent event) {
        try {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            int user_id = callUsersId(player);
            World world = block.getWorld();
            int world_id = callWorldsId(world);
            int X = block.getX();
            int Y = block.getY();
            int Z = block.getZ();
            int b_id = block.getTypeId();
            int b_subid = block.getData();

            String sql = "INSERT INTO " + block_places + " (user_id,world_id,x,y,z,block_id,block_subid,time) VALUES (?,?,?,?,?,?,?,now())";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, world_id);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, b_id);
            statement.setInt(7, b_subid);
            statement.executeUpdate();
            //plugin.getServer().broadcastMessage(ChatColor.GREEN +  "MySQLに記録しました");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBreakLog(BlockBreakEvent event) {
        try {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            int user_id = callUsersId(player);
            World world = block.getWorld();
            int world_id = callWorldsId(world);
            int X = block.getX();
            int Y = block.getY();
            int Z = block.getZ();
            int b_id = block.getTypeId();
            int b_subid = block.getData();

            String sql = "INSERT INTO " + block_breaks + " (user_id,world_id,x,y,z,block_id,block_subid,time) VALUES (?,?,?,?,?,?,?,now())";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, world_id);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, b_id);
            statement.setInt(7, b_subid);
            statement.executeUpdate();
            //plugin.getServer().broadcastMessage(ChatColor.GREEN +  "MySQLに記録しました");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ここから呼び出し
    public boolean playerExists(Player player) {
        try {
            String name = player.getName();
            String uuid = player.getUniqueId().toString();

            String sql = "SELECT * FROM " + users + " WHERE UUID=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if (results.next()) { //プレイヤーが存在する時
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "プレイヤーが見つかりました: " + ChatColor.WHITE + name);
                return true;
            } else {
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "プレイヤーが見つかりません: " + ChatColor.WHITE + name);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return false;
    }

    public boolean worldsExists(World world) {
        try {
            String name = world.getName();

            String sql = "SELECT * FROM " + worlds + " WHERE name=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();

            if (results.next()) { //ワールドが存在する時
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "ワールドが見つかりました: " + ChatColor.WHITE + name);
                return true;
            } else {
                plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "ワールドが見つかりません: " + ChatColor.WHITE + name);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return false;
    }

    public int callUsersId(Player player) {
        try {
            String uuid = player.getUniqueId().toString();

            String sql = "SELECT * FROM " + users + " WHERE UUID=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return 0;
    }

    public int callWorldsId(World world) {
        try {
            String name = world.getName();

            String sql = "SELECT * FROM " + worlds + " WHERE name=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return 0;
    }

    public java.sql.Time callLatestLoginDateTime(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM logins WHERE user_id=? ORDER BY DateTime DESC LIMIT 1";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getTime("DateTime");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return null;
    }

    public java.sql.Time callSecondLoginDateTime(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM logins WHERE user_id=? ORDER BY DateTime DESC LIMIT 1 OFFSET 1";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getTime("DateTime");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return null;
    }

    public int callTrueLoginTimes(Player player) {
        try {
            int user_id = callUsersId(player);
            String sql = "SELECT COUNT(*) FROM logins WHERE user_id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return 0;
    }

    public boolean isAB(Block block) {
        //置かれていたブロックと壊したブロックのログが一致した場合、人工ブロックと判断する
        try {
            World world = block.getWorld();
            int world_id = callWorldsId(world);
            int X = block.getX();
            int Y = block.getY();
            int Z = block.getZ();
            int b_id = block.getTypeId();
            int b_subid = block.getData();

            String sql = "SELECT EXISTS(SELECT * FROM block_places WHERE world_id=? and X=? and Y=? and Z=? and block_id=? and block_subid=?)";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, world_id);
            statement.setInt(2, X);
            statement.setInt(3, Y);
            statement.setInt(4, Z);
            statement.setInt(5, b_id);
            statement.setInt(6, b_subid);
            ResultSet results = statement.executeQuery();
            results.next();
            if (1 == results.getInt(1)) {
                return true; //Artificial Block (人工ブロック)
            } else if (0 == results.getInt(1)) {
                return false; //Natural Block (自然ブロック)
            } else {
                outError();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return false;
    }

    public boolean isPlacedByMe(BlockBreakEvent event) {
        //壊したブロックが自分のおいたブロックだった場合をtrueを返す
        try {
            Player player = event.getPlayer();
            int user_id = callUsersId(player);
            Block block = event.getBlock();
            World world = block.getWorld();
            int world_id = callWorldsId(world);
            int X = block.getX();
            int Y = block.getY();
            int Z = block.getZ();
            int b_id = block.getTypeId();
            int b_subid = block.getData();

            String sql = "SELECT EXISTS(SELECT * FROM block_places WHERE user_id=? and world_id=? and X=? and Y=? and Z=? and block_id=? and block_subid=?)";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, world_id);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, b_id);
            statement.setInt(7, b_subid);
            ResultSet results = statement.executeQuery();
            results.next();
            if (1 == results.getInt(1)) {
                return true; //自分の置いたブロック
            } else if (0 == results.getInt(1)) {
                return false; //他人が置いたブロック
            } else {
                outError();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return false;
    }

    public int callLoginTimes(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM " + users + " WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("login_times");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return -1;
    }

    public long callElapsedTimeSinceLogin(Player player) {
        LocalTime localTime = LocalTime.now();
        java.sql.Time javaSqlTime = java.sql.Time.valueOf(localTime);
        long nowTime = javaSqlTime.getTime();
        long loginTime = callLatestLoginDateTime(player).getTime();

        long DiffMinutes = (nowTime - loginTime) / (1000 * 60);
        return DiffMinutes;
    }

    public java.sql.Time callLatestBreakDateTime(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM block_breaks WHERE user_id=? ORDER BY time DESC LIMIT 1";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getTime("time");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return null;
    }

    public java.sql.Time callSecondBreakDateTime(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM block_breaks WHERE user_id=? ORDER BY time DESC LIMIT 1 OFFSET 1";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getTime("time");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return null;
    }

    public int callBreakTimes(Player player) {
        try {
            int user_id = callUsersId(player);
            String sql = "SELECT COUNT(*) FROM block_breaks WHERE user_id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return 0;
    }

    public int callBreakTimesFrom(Player player, int sec) {
        try {
            int user_id = callUsersId(player);
            String sql = "SELECT COUNT(*) FROM block_breaks WHERE user_id=? AND time < NOW() AND time > SUBDATE(NOW(), INTERVAL " + sec +" SECOND)";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return 0;
    }

    public String callUUIDofPlacedByWho(Block block) {
        try {
            World world = block.getWorld();
            int world_id = callWorldsId(world);
            int X = block.getX();
            int Y = block.getY();
            int Z = block.getZ();
            int b_id = block.getTypeId();
            int b_subid = block.getData();
            if (isAB(block)) {
                String sql = "SELECT * FROM block_places WHERE world_id=? and X=? and Y=? and Z=? and block_id=? and block_subid=? ORDER BY time DESC LIMIT 1";
                PreparedStatement statement = getConnection().prepareStatement(sql);
                statement.setInt(1, world_id);
                statement.setInt(2, X);
                statement.setInt(3, Y);
                statement.setInt(4, Z);
                statement.setInt(5, b_id);
                statement.setInt(6, b_subid);
                ResultSet results = statement.executeQuery();
                results.next();
                return callUUIDbyUsersID(results.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return "error";
    }

    public String callUUIDbyUsersID(int user_id) {
        try {
            String sql = "SELECT * FROM " + users + " WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString("uuid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return "error";
    }

    //ここからGP関連
    public void addGP(Player player, double gp) {
        try {
            int user_id = callUsersId(player);
            String name = player.getName();

            String sql = "UPDATE " + users + " SET GP=GP+(?) WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setDouble(1, gp);
            statement.setInt(2, user_id);
            statement.executeUpdate();
            //plugin.getServer().broadcastMessage(ChatColor.RED + name + "のGPが" + gp + "増えました");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setGP(Player player, double gp) {
        try {
            int user_id = callUsersId(player);

            String sql = "UPDATE " + users + " SET GP=(?) WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setDouble(1, gp);
            statement.setInt(2, user_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkGP(Player player) {
        try {
            String name = player.getDisplayName();
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM " + users + " WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            plugin.getServer().broadcastMessage(AutoAntiGriefing.prefix + name + "の現在のGPは" + results.getInt("GP") + "です。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int callGP(Player player) {
        try {
            int user_id = callUsersId(player);

            String sql = "SELECT * FROM " + users + " WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("GP");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outError();
        return -1;
    }

    public void runnable(){
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player online: Bukkit.getOnlinePlayers()) {
                    int nowGP = callGP(online);
                    int changeGP = 150;
                    if(nowGP - changeGP < 0) {
                        setGP(online, 0);
                    } else {
                        addGP(online, -changeGP);
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 100);
    }

    //ここからコマンド
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //gpコマンドの場合
        if (command.getName().equalsIgnoreCase(cmd1)) {
            //プレイヤー送信の場合
            if (sender instanceof Player) {
                Player senderPlayer = (Player) sender;
                //略の場合
                if (args.length == 0) {
                    int gp = callGP(senderPlayer);
                    String name = senderPlayer.getName();
                    senderPlayer.sendMessage(AutoAntiGriefing.prefix + ChatColor.WHITE + name + ChatColor.GREEN + "の現在のGPは" + ChatColor.WHITE + gp + ChatColor.GREEN + "です。");
                    return true;
                //略以外の場合
                } else if (args.length == 1) {
                    for (OfflinePlayer targetPlayer: Bukkit.getOfflinePlayers()) {
                        String name = targetPlayer.getName();
                        if (args[0].equalsIgnoreCase(name)) {
                            int gp = callGP(targetPlayer.getPlayer());
                            senderPlayer.sendMessage(AutoAntiGriefing.prefix + ChatColor.WHITE + name + ChatColor.GREEN + "の現在のGPは" + ChatColor.WHITE + gp + ChatColor.GREEN + "です。");
                            return true;
                        } else {
                            senderPlayer.sendMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "そのプレイヤーは存在しません");
                            return true;
                        }
                    }
                }
            // サーバーコマンドの場合
            } else {
                if (args.length == 0) {
                    sender.sendMessage(AutoAntiGriefing.prefix + "このコマンドはプレイヤーしか使えません");
                    return true;
                } else if (args.length == 1) {
                    for (OfflinePlayer targetPlayer: Bukkit.getOfflinePlayers()) {
                        String name = targetPlayer.getName();
                        if (args[0].equalsIgnoreCase(name)) {
                            int gp = callGP(targetPlayer.getPlayer());
                            sender.sendMessage(AutoAntiGriefing.prefix + ChatColor.WHITE + name + ChatColor.GREEN + "の現在のGPは" + ChatColor.WHITE + gp + ChatColor.GREEN + "です。");
                            return true;
                        } else {
                            sender.sendMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "そのプレイヤーは存在しません");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //ここから計算ブロック
    @EventHandler
    public void MathOnBreakAndKick(BlockBreakEvent e) {
        addBreakLog(e); //callElapsedTimeSinceBreakを正常に作動させるため最初に
        Player player = e.getPlayer();
        String name = player.getName();

        Double math_result = (
                10
                        * mag_TypeOfBlock(e)
                        * mag_WhetherABorNB(e)
                        * mag_WhetherMine(e)
                        * mag_WhetherNewPlayer(e)
                        * mag_ElapsedTime(e)
                        * mag_Distance(e)
                        * mag_ContinuousBreak(e)
                        * mag_EfficiencyBreak(e)
        );

        addGP(player, math_result);

        /*plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "-< Chiji Plugin >---------");
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "--<" + name + ">--");
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  TypeOfBlock: " + mag_TypeOfBlock(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  WhetherABorNB: " + mag_WhetherABorNB(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  WhetherMine: " + mag_WhetherMine(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  WhetherNewPlayer: " + mag_WhetherNewPlayer(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  ElapseTime: " + mag_ElapsedTime(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  Distance: " + mag_Distance(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  ContinuousBreak: " + mag_ContinuousBreak(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + "  EfficiencyBreak: " + mag_EfficiencyBreak(e));
        plugin.getServer().getConsoleSender().sendMessage(AutoAntiGriefing.prefix + name + "のGPが" + math_result + "増えました");*/

        int gp = callGP(player);
        if (gp > 50000) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "[AAG] Griefingと判定されたためBANしました", null, null);
            plugin.getServer().broadcastMessage(AutoAntiGriefing.prefix + ChatColor.WHITE + name + ChatColor.RED + "のGPが規定値を超えたためBANしました");
        } else if (gp > 42500) {
            plugin.getServer().broadcastMessage(AutoAntiGriefing.prefix + ChatColor.WHITE + name + ChatColor.YELLOW + "は荒らしの可能性があります");
        } else if (gp > 35000) {
            plugin.getServer().broadcastMessage(AutoAntiGriefing.prefix + ChatColor.YELLOW + "誰かが荒らしているかも？");
        }
    }

    public double mag_TypeOfBlock(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();

        switch (blockType) {
            case WHEAT:
            case CACTUS:
            case SUGAR_CANE:
            case SUGAR_CANE_BLOCK:
            case SEEDS:
            case BEETROOT_SEEDS:
            case MELON_SEEDS:
            case PUMPKIN_SEEDS:
            case NETHER_WART_BLOCK:
            case NETHER_WARTS:
            case CROPS:
            case COCOA:
            case CARROT:
            case CARROT_ITEM:
            case POTATO:
            case POTATO_ITEM:
            case CHORUS_PLANT:
                return 0.1;
            case DIRT:
            case GRASS:
            case STONE:
            case LOG:
            case LOG_2:
                return 0.5;
            case ENDER_CHEST:
            case IRON_DOOR:
            case IRON_DOOR_BLOCK:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case TRAPPED_CHEST:
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:
            case REDSTONE:
            case REDSTONE_BLOCK:
            case REDSTONE_COMPARATOR:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_LAMP_OFF:
            case REDSTONE_LAMP_ON:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case REDSTONE_WIRE:
            case IRON_TRAPDOOR:
                return 3;
            case CHEST:
            case FURNACE:
            case SIGN:
                return 5;
            case COMMAND:
            case COMMAND_CHAIN:
            case COMMAND_REPEATING:
            case COMMAND_MINECART:
            case BARRIER:
            case BEDROCK:
                return 10;
            default:
                return 1;
        }
    }

    public double mag_WhetherABorNB(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (isAB(block)) {
            return 3;
        }
        return 1;
    }

    public double mag_WhetherMine(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (isAB(block) && isPlacedByMe(event)) {
            return 0;
        }
        return 1;
    }

    public double mag_WhetherNewPlayer(BlockBreakEvent event) {
        Player player = event.getPlayer();

        switch (callLoginTimes(player)) {
            case 1:
                return 3;
            case 2:
                return 1.5;
            case 3:
                return 1.3;
            case 4:
                return 1.2;
            case 5:
                return 1.1;
            default:
                return 1;
        }
    }

    public double mag_ElapsedTime(BlockBreakEvent event) {
        Player player = event.getPlayer();
        long minutes = callElapsedTimeSinceLogin(player);

        if (minutes < 1) {
            return 7;
        } else if (minutes < 2) {
            return 5;
        } else if (minutes < 3) {
            return 3;
        } else if (minutes < 5) {
            return 2;
        } else if (minutes < 10) {
            return 1.5;
        } else if (minutes < 30) {
            return 1.3;
        } else if (minutes < 60) {
            return 1.2;
        } else if (minutes < 120) {
            return 1.1;
        } else {
            return 1;
        }
    }

    public double mag_Distance(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (isAB(block) && !isPlacedByMe(event)) {
            Location breaker_loc = event.getPlayer().getLocation();
            String placer_UUID = callUUIDofPlacedByWho(block);
            for (Player online: Bukkit.getOnlinePlayers()) {
                // もし被プレイヤーがサーバー内にいた時
                if(online.getUniqueId().toString() == placer_UUID) {
                    Location placer_loc = online.getLocation();
                    Double distance = breaker_loc.distance(placer_loc);
                    if (distance < 5) {
                        return 0.1;
                    } else if (distance < 10) {
                        return 0.5;
                    } else if (distance < 20) {
                        return 1;
                    } else if (distance < 50) {
                        return 1.25;
                    } else if (distance < 100) {
                        return 2;
                    } else if (distance < 500) {
                        return 4;
                    } else if (distance < 1000) {
                        return 6;
                    } else {
                        return 8;
                    }
                } else { //オフラインだったら
                    return 10;
                }
            }
        } else {
            return 1;
        }
        outError();
        return -1;
    }

    public double mag_EfficiencyBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (isAB(block) && callBreakTimes(player) >= 2 && !isPlacedByMe(event)) {
            long dateTimeTo = callLatestBreakDateTime(player).getTime();
            long dateTimeFrom = callSecondBreakDateTime(player).getTime();
            // 差分の時間を算出します。
            long DiffSeconds = ( dateTimeTo - dateTimeFrom ) / 1000;

            if (DiffSeconds < 0.3) {
                return 2;
            } else if (DiffSeconds < 0.5) {
                return 1.6;
            } else if (DiffSeconds < 1) {
                return 1.3;
            } else {
                return 1;
            }
        }
        return 1;
    }

    public double mag_ContinuousBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(isAB(block) && callBreakTimes(player) >= 2 && !isPlacedByMe(event)) {
            int breakTimesIn10 = callBreakTimesFrom(player, 10);
            if (breakTimesIn10 > 20) {
                return 3;
            } else if (breakTimesIn10 > 16) {
                return 2.5;
            } else if (breakTimesIn10 > 12) {
                return 2;
            } else if (breakTimesIn10 > 8) {
                return 1.5;
            } else if (breakTimesIn10 > 4) {
                return 1.2;
            } else {
                return 1;
            }
        }
        return 1;
    }

    //例外処理
    public void outError() {
        System.out.printf("エラー");
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.shutdown();
    }

}
