using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.IO.Compression;
using System.Linq;

namespace EVTC_Log_Parser.Model
{
    public class Parser
    {
        #region Members
        private BinaryReader _br;
        #endregion

        #region Properties
        public Metadata Metadata { get; set; }
        public List<Player> Players { get; set; }
        public List<NPC> NPCs { get; set; }
        public List<Gadget> Gadgets { get; set; }
        public List<Skill> Skills { get; set; }
        public List<Event> Events { get; set; }
        #endregion

        #region Public Methods
        public bool Parse(string filePath)
        {
            try
            {
                if (Path.GetExtension(filePath) == ".evtc")
                {
                    _br = new BinaryReader(new FileStream(filePath, FileMode.Open, FileAccess.Read));
                }
                else
                {
                    Stream stream = ZipFile.OpenRead(filePath).Entries[0].Open();
                    MemoryStream memStream = new MemoryStream();
                    stream.CopyTo(memStream);
                    memStream.Position = 0;
                    _br = new BinaryReader(memStream);
                }
                using (_br)
                {
                    Metadata = new Metadata();
                    Players = new List<Player>();
                    NPCs = new List<NPC>();
                    Gadgets = new List<Gadget>();
                    Skills = new List<Skill>();
                    Events = new List<Event>();
                    ParseMetadata();
                    if (int.Parse(Metadata.ARCVersion.Substring(4)) < 20170218) { return false; }
                    ParseAgents();
                    ParseSkills();
                    ParseEvents();
                    FillMissingData();
                    return true;
                }
            }
            catch { return false; }
        }
        #endregion

        #region Private Methods
        private void ParseMetadata()
        {
            Metadata.ARCVersion = _br.ReadUTF8(12); // 12 bytes: build version 
            Metadata.TargetSpeciesId = _br.Skip(1).ReadUInt16(); // 2 bytes: instid
        }

        private void ParseAgents()
        {
            // 4 bytes: agent count
            int ac = _br.Skip(1).ReadInt32();

            // 96 bytes: each agent
            for (int i = 0; i < ac; i++)
            {
                // add agent
                AddAgent();
            }
        }

        private void ParseSkills()
        {
            // 4 bytes: skill count
            int sc = _br.ReadInt32();

            // 68 bytes: each skill
            for (int i = 0; i < sc; i++)
            {
                Skills.Add(new Skill()
                {
                    Id = _br.ReadInt32(), // 4 bytes: id
                    Name = _br.ReadUTF8(64) // 64 bytes: name
                });
            }
        }

        private void ParseEvents()
        {
            // Until EOF
            while (_br.BaseStream.Position != _br.BaseStream.Length)
            {
                Event combat = new Event()
                {
                    Time = (int)_br.ReadUInt64(), // 8 bytes: Time
                    SrcAgent = _br.ReadUInt64Hex(), // 8 bytes: Src Agent
                    DstAgent = _br.ReadUInt64Hex(),  // 8 bytes: Dst Agent
                    Value = _br.ReadInt32(), // 4 bytes: Value
                    BuffDmg = _br.ReadInt32(), // 4 bytes: Buff Damage
                    OverstackValue = _br.ReadUInt16(), // 2 bytes: Overstack Value
                    SkillId = _br.ReadUInt16(), // 2 bytes: Skill Id
                    SrcInstid = _br.ReadUInt16(), // 2 bytes: Src Instid
                    DstInstid = _br.ReadUInt16(), // 2 bytes: Dst Instid
                    SrcMasterInstid = _br.ReadUInt16(), // 2 bytes: Src Master Instid
                    IFF = (IFF)_br.Skip(9).Read(), // 1 byte: IFF
                    IsBuff = _br.ReadBoolean(), // 1 byte: IsBuff
                    Result = (Result)_br.Read(), // 1 byte: Result
                    Activation = (Activation)_br.Read(), // 1 byte: Activation
                    BuffRemove = (BuffRemove)_br.Read(), // 1 byte: BuffRemove
                    IsNinety = _br.ReadBoolean(), // 1 byte: IsNinety
                    IsFifty = _br.ReadBoolean(), // 1 byte: IsFifty
                    IsMoving = _br.ReadBoolean(), // 1 byte: IsMoving
                    StateChange = (StateChange)_br.Read(), // 1 byte: StateChange
                    IsFlanking = _br.ReadBoolean(), // 1 byte: IsFlanking
                    IsShield = _br.ReadBoolean() // 1 byte: IsShield
                };

                // Add Combat
                _br.Skip(2);
                Events.Add(combat);
            }
        }

        private Profession ParseProfession(int pLower, int pUpper, int isElite)
        {
            if (isElite == -1)
            {
                if (pUpper == 65535)
                {
                    return Profession.Gadget;
                }
                else
                {
                    return Profession.NPC;
                }
            }
            else
            {
                return (Profession)pLower + (9 * isElite);
            }
        }

        private void AddAgent()
        {
            string address = _br.ReadUInt64Hex(); // 8 bytes: Address
            int pLower = BitConverter.ToUInt16(_br.ReadBytes(2), 0); // 2 bytes: Prof (lower bytes)
            int pUpper = BitConverter.ToUInt16(_br.ReadBytes(2), 0); // 2 bytes: prof (upper bytes)
            int isElite = _br.ReadInt32(); // 4 bytes: IsElite
            int toughness = _br.ReadInt32();  // 4 bytes: Toughness
            int healing = _br.ReadInt32();  // 4 bytes: Healing
            int condition = _br.ReadInt32();  // 4 bytes: Condition
            string name = _br.ReadUTF8(68); // 68 bytes: Name

            // Add Agent by Type
            Profession profession = ParseProfession(pLower, pUpper, isElite);
            switch (profession)
            {
                case Profession.Gadget:
                    Gadgets.Add(new Gadget()
                    {
                        Address = address,
                        PseudoId = pLower,
                        Name = name
                    });
                    return;
                case Profession.NPC:
                    NPCs.Add(new NPC()
                    {
                        Address = address,
                        SpeciesId = pLower,
                        Toughness = toughness,
                        Healing = healing,
                        Condition = condition,
                        Name = name
                    });
                    return;
                default:
                    Players.Add(new Player(name)
                    {
                        Address = address,
                        Profession = profession,
                        Toughness = toughness,
                        Healing = healing,
                        Condition = condition,
                    });
                    return;
            }
        }

        private void FillMissingData()
        {
            // Update Instid
            foreach (Player p in Players)
            {
                foreach (Event e in Events)
                {
                    if (p.Address == e.SrcAgent && e.SrcInstid != 0)
                    {
                        p.Instid = e.SrcInstid;
                    }
                    else if (p.Address == e.DstAgent && e.DstInstid != 0)
                    {
                        p.Instid = e.DstInstid;
                    }
                }
            }
            foreach (NPC n in NPCs)
            {
                foreach (Event e in Events)
                {
                    if (n.Address == e.SrcAgent && e.SrcInstid != 0)
                    {
                        n.Instid = e.SrcInstid;
                    }
                    else if (n.Address == e.DstAgent && e.DstInstid != 0)
                    {

                        n.Instid = e.DstInstid;
                    }
                }
            }

            // Update Metadata
            IEnumerable<Event> sc = Events.Where(e => e.StateChange != StateChange.None);
            foreach (Event e in sc)
            {
                StateChange s = e.StateChange;
                if (s == StateChange.LogStart)
                {
                    Metadata.LogStart = DateTimeOffset.FromUnixTimeSeconds(e.Value).DateTime;
                }
                else if (s == StateChange.LogEnd)
                {
                    Metadata.LogEnd = DateTimeOffset.FromUnixTimeSeconds(e.Value).DateTime;
                }
                else if (s == StateChange.PointOfView)
                {
                    Metadata.PointOfView = (Players.Find(p => p.Address == e.SrcAgent) != null) ? Players.Find(p => p.Address == e.SrcAgent).Account : ":?.????";
                }
                else if (s == StateChange.Language)
                {
                    Metadata.Language = (Language)e.Value;
                }
                else if (s == StateChange.GWBuild)
                {
                    Metadata.GWBuild = int.Parse(e.SrcAgent, NumberStyles.HexNumber);
                }
                else if (s == StateChange.ShardID)
                {
                    Metadata.ShardID = int.Parse(e.SrcAgent, NumberStyles.HexNumber);
                }
            }

            // Time normalization
            int ts = Events[0].Time;
            Events.ForEach(e => e.Time -= ts);

            // Target
            NPC tg = NPCs.Find(n => n.SpeciesId == Metadata.TargetSpeciesId);

            // Adjust instids of second half of Xera
            if (tg.SpeciesId == 16246)
            {
                NPC shx = NPCs.Find(n => n.SpeciesId == 16286);
                if (shx != null)
                {
                    foreach (Event e in Events)
                    {
                        if (e.SrcInstid == shx.Instid)
                        {
                            e.SrcInstid = tg.Instid;
                        }
                        else if (e.DstInstid == shx.Instid)
                        {
                            e.DstInstid = tg.Instid;
                        }
                        else if (e.SrcMasterInstid == shx.Instid)
                        {
                            e.SrcMasterInstid = tg.Instid;
                        }
                    }
                }
            }

            // Set First/Last Aware for Target
            tg.FirstAware = Events.Where(e => e.SrcInstid == tg.Instid).First().Time;
            Event tde = Events.Find(e => e.StateChange == StateChange.ChangeDead && e.SrcInstid == tg.Instid);
            if (tde != null)
            {
                tg.LastAware = tde.Time;
                Events = Events.TakeWhile(e => !(e.StateChange == StateChange.ChangeDead && e.SrcInstid == tg.Instid)).ToList(); // Trim Events After Death
            }
            else
            {
                tg.LastAware = Events.Where(e => e.SrcInstid == tg.Instid).Last().Time;
            }
        }
        #endregion
    }
}
